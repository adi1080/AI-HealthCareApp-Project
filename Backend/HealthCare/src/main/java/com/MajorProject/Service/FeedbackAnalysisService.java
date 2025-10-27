package com.MajorProject.Service;

import com.MajorProject.Client.StreamAI;
import com.MajorProject.Entity.Feedback;
import com.MajorProject.Repository.FeedbackRepository;
import com.MajorProject.Repository.UserRepository;
import com.MajorProject.common.Domain.AIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FeedbackAnalysisService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StreamAI streamAI;

    @Async
    public void analyzeDoctorFeedback(Long doctorId) {
        try {
            // 1️⃣ Get the doctor (User)
            var doctorOpt = userRepository.findById(doctorId);
            if (doctorOpt.isEmpty()) {
                System.err.println("❌ Doctor not found: " + doctorId);
                return;
            }
            var doctor = doctorOpt.get();

            // 2️⃣ Determine last analyzed feedback ID
            Long lastAnalyzedId = doctor.getLastAnalyzedFeedbackId();
            List<Feedback> newFeedbacks;

            if (lastAnalyzedId == null) {
                // First-time analysis → analyze all
                newFeedbacks = feedbackRepository.findByDoctor_Id(doctorId);
            } else {
                // Incremental → only new ones
                newFeedbacks = feedbackRepository.findNewFeedbackAfterId(doctorId, lastAnalyzedId);
            }

            if (newFeedbacks.isEmpty()) {
                System.out.println("ℹ️ No new feedback for doctorId: " + doctorId);
                return;
            }

            // 3️⃣ Convert to comment list
            List<String> newFeedbackComments = newFeedbacks.stream()
                    .map(Feedback::getFeedbackComment)
                    .toList();

            // 4️⃣ Combine old summary (if exists) with new feedback
            String existingSummary = doctor.getFeedbackAnalysis() != null ? doctor.getFeedbackAnalysis() : "";
            String combinedPrompt = "Previous summary: " + existingSummary + "\n\nNew feedback:\n" + String.join("\n", newFeedbackComments);

            // 5️⃣ Call AI
            Map<Long, AIResponse> result = streamAI.analyzeFeedbacks(Map.of(doctorId, List.of(combinedPrompt)));

            // 6️⃣ Save new summary and update last analyzed feedback ID
            AIResponse aiResponse = result.get(doctorId);
            if (aiResponse != null) {
                doctor.setFeedbackAnalysis(aiResponse.getSummary());

                // Update last analyzed ID to the latest feedback
                Long maxFeedbackId = newFeedbacks.stream()
                        .mapToLong(Feedback::getFeedbackid)
                        .max()
                        .orElse(lastAnalyzedId != null ? lastAnalyzedId : 0);
                doctor.setLastAnalyzedFeedbackId(maxFeedbackId);

                userRepository.save(doctor);
            }

            System.out.println("✅ Incremental feedback analysis updated for doctorId: " + doctorId);

        } catch (Exception e) {
            System.err.println("❌ Error analyzing feedback for doctorId " + doctorId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Async
    public void analyzeMissingOrOutdatedFeedbacks() {
        try {
            var doctors = userRepository.findAll()
                    .stream()
                    .filter(u -> "doctor".equalsIgnoreCase(u.getRole()))
                    .toList();

            for (var doctor : doctors) {
                Long lastAnalyzedId = doctor.getLastAnalyzedFeedbackId();

                // Check latest feedback ID for this doctor
                Long latestFeedbackId = feedbackRepository.findLatestFeedbackIdByDoctor_Id(doctor.getId());

                // Skip if no feedback at all
                if (latestFeedbackId == null) continue;

                boolean needsAnalysis = (doctor.getFeedbackAnalysis() == null)
                        || (lastAnalyzedId == null)
                        || (latestFeedbackId > lastAnalyzedId);

                if (needsAnalysis) {
                    System.out.println("🔍 Doctor " + doctor.getId() + " needs analysis. Running...");
                    analyzeDoctorFeedback(doctor.getId());
                }
            }

            System.out.println("✅ Smart feedback analysis completed.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Smart feedback analysis failed: " + e.getMessage());
        }
    }

}

