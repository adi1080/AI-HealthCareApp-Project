package com.MajorProject.Service;

import com.MajorProject.Client.StreamAI;
import com.MajorProject.Domain.AIRequest;
import com.MajorProject.Domain.AIResponse;
import com.MajorProject.Entity.Patient;
import jakarta.annotation.PreDestroy;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AIAnalysis {

    @Autowired
    private StreamAI aiClient;

    @Autowired
    private PatientService ps;

    public final ExecutorService aiAnalysisExecutor = Executors.newSingleThreadExecutor();

    public void analyzeInBackground(Patient patient) {
        aiAnalysisExecutor.submit(() -> {
            try {
                Path filePath = Path.of("F:/Pendrive Stuff/Major-Project/reports/" + patient.getReportFilePath());
                String reportContent = extractTextFromFile(filePath);

                if (reportContent == null || reportContent.isEmpty()) {
                    System.err.println("❌ Report file is empty or unsupported format: " + filePath);
                    return;
                }

                AIRequest aiRequest = new AIRequest(reportContent);
                AIResponse aiResponse = aiClient.analyzeReport(aiRequest);

                patient.setHealthSummary(aiResponse.getSummary());

                // Convert List<String> to single string with line breaks (or commas, as preferred)
                String joinedSuggestions = String.join("\n", aiResponse.getSuggestions());
                patient.setHealthSuggestions(joinedSuggestions);

                patient.setHealthScore(aiResponse.getHealthScore());
                patient.setAiAnalysisDone(true);

                ps.saveProfile(patient);

                System.out.println("✅ AI analysis completed for patient " + patient.getId());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private String extractTextFromFile(Path filePath) throws Exception {
        String fileName = filePath.getFileName().toString().toLowerCase();

        if (fileName.endsWith(".txt")) {
            // Plain text
            return Files.readString(filePath);
        } else if (fileName.endsWith(".pdf")) {
            // PDF using Loader.loadPDF()
            try (PDDocument document = Loader.loadPDF(filePath.toFile())) {
                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(document);
            }
        } else if (fileName.endsWith(".docx")) {
            // Word document
            try (InputStream is = new FileInputStream(filePath.toFile())) {
                XWPFDocument doc = new XWPFDocument(is);
                StringBuilder text = new StringBuilder();
                List<XWPFParagraph> paragraphs = doc.getParagraphs();
                for (XWPFParagraph para : paragraphs) {
                    text.append(para.getText()).append("\n");
                }
                return text.toString();
            }
        } else {
            System.err.println("Unsupported file type: " + fileName);
            return null;
        }
    }

    @PreDestroy
    public void shutdownExecutor() {
        aiAnalysisExecutor.shutdown();
    }
}
