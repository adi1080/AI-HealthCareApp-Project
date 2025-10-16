package com.MajorProject.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.MajorProject.Entity.*;
import com.MajorProject.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MajorProject.Domain.AppointmentDTO;
import com.MajorProject.Domain.DoctorDTO;
import com.MajorProject.Domain.FeedbackDTO;
import com.MajorProject.Domain.PatientDTO;

@Service
public class PatientService {

    @Autowired
    PatientRepository pr;

    @Autowired
    FeedbackRepository fb;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    DoctorAvailabilityRepository availabilityRepository;

    public Patient saveProfile(Patient patient) {
        return pr.save(patient);
    }

    public boolean checkprofileExistsOrNot(long id) {
        if (pr.existsById(id)) {
            return true;
        } else {
            return false;
        }
    }

    public Optional<Patient> FindById(long id) {
        return pr.findById(id);
    }

    public boolean existsByAvailability(DoctorAvailability availability) {
        return appointmentRepository.existsByAvailability(availability);
    }

    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public Feedback addFeedback(Feedback feedback) {
        return fb.save(feedback);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<FeedbackDTO> getFeedbacks(long doctorId) {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        if (doctor.isEmpty()) {
            return Collections.emptyList();
        }

        List<Feedback> feedbacks = fb.findByDoctor(doctor.get());
        List<FeedbackDTO> feedbackDTOs = new ArrayList<>();

        for (Feedback feedback : feedbacks) {
            FeedbackDTO dto = new FeedbackDTO();
            dto.setFeedbackId(feedback.getFeedbackid());
            dto.setFeedbackComment(feedback.getFeedbackComment());
            dto.setRating(feedback.getRating());
            dto.setDate(feedback.getDate().toString());

            Doctor doctorEntity = feedback.getDoctor();
            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setId(doctorEntity.getId());
            doctorDTO.setName(doctorEntity.getName());
            doctorDTO.setSpeciality(doctorEntity.getSpeciality());
            doctorDTO.setExperience(doctorEntity.getExperience());


            Patient patientEntity = feedback.getPatient();
            if (patientEntity != null) {
                PatientDTO patientDTO = new PatientDTO();
                patientDTO.setId(patientEntity.getId());
                patientDTO.setName(patientEntity.getName());
                dto.setPatient(patientDTO);
            }

            dto.setDoctor(doctorDTO);
            feedbackDTOs.add(dto);
        }

        return feedbackDTOs;
    }

    public PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setAge(patient.getAge());
        dto.setGender(patient.getGender());
        dto.setAddress(patient.getAddress());
        dto.setMobileno(patient.getMobileno());
        dto.setHistory(patient.getHistory());
        // Use null-safe values for AI fields
        dto.setHealthSummary(patient.getHealthSummary() != null ? patient.getHealthSummary() : "AI analysis pending...");
        dto.setHealthSuggestions(patient.getHealthSuggestions() != null ? patient.getHealthSuggestions() : "Suggestions not available yet.");
        dto.setHealthScore(patient.getHealthScore() != null ? patient.getHealthScore() : 0.0);  // or 0.0 if preferred
        dto.setAiAnalysisDone(Boolean.TRUE.equals(patient.getAiAnalysisDone()));

        List<AppointmentDTO> appointmentDTOs = patient.getAppointments().stream().map(appointment -> {
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setAppointmentId(appointment.getAppointmentId());
            appointmentDTO.setDoctorCity(appointment.getDoctor().getCity());
            appointmentDTO.setReason(appointment.getReason());
            appointmentDTO.setStatus(appointment.getStatus().getStatus());

            // Fetch doctor info
            Doctor doctor = appointment.getDoctor();
            appointmentDTO.setDoctorId(doctor.getId());
            appointmentDTO.setDoctorName(doctor.getName());
            appointmentDTO.setClinicAddress(doctor.getClinicAddress());

            // Fetch availability info
            DoctorAvailability availability = appointment.getAvailability();
            appointmentDTO.setAvailabilityId(availability.getId());
            appointmentDTO.setAvailabilityDatetime(availability.getDatetime());

            return appointmentDTO;
        }).collect(Collectors.toList());

        dto.setAppointments(appointmentDTOs);

        // Add this line:
        dto.setReportFilePath(patient.getReportFilePath());

        return dto;
    }

    public void deleteAppointment(long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        DoctorAvailability bookedTimeSlot = appointment.get().getAvailability();
        appointmentRepository.deleteById(id);
        availabilityRepository.delete(bookedTimeSlot);
    }

    public Optional<Feedback> findFeedbackByDoctor_IdAndPatient_Id(long id, long id1) {
        return fb.findByDoctor_IdAndPatient_Id(id, id1);
    }

    public void deleteFeedback(Feedback feedback) {
        fb.delete(feedback);
    }

    public void updateMisconductAndReason(long id) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (!appointmentOpt.isPresent()) {
            return;
        }

        Appointment appointment = appointmentOpt.get();
        DoctorAvailability bookedTimeSlot = appointment.getAvailability();
        User user = appointment.getPatient().getUser();

        LocalDateTime now = LocalDateTime.now();

        // Reset cancellations if a new month
        if (user.getLastCancelReset() == null ||
                user.getLastCancelReset().getMonth() != now.getMonth() ||
                user.getLastCancelReset().getYear() != now.getYear()) {

            user.setAppointmentsCanceled(0);
            user.setMisconductScore(0);
            user.setMisconductReason("");
            user.setLastCancelReset(now);
        }

        // If cancellation happens less than 5 minutes before appointment
        if (ChronoUnit.MINUTES.between(now, bookedTimeSlot.getDatetime()) <= 5) {
            user.setAppointmentsCanceled(user.getAppointmentsCanceled() + 1);
            userRepository.save(user);
        }

        if (user.getAppointmentsCanceled() >= 5) {
            int randomScore = ThreadLocalRandom.current().nextInt(40, 60);

            if (user.getMisconductScore() == 0) {
                user.setMisconductScore(randomScore);
            } else {
                user.setMisconductScore(user.getMisconductScore() + randomScore);
            }

            user.setMisconductReason("Canceled " + user.getAppointmentsCanceled() + " appointments");
            userRepository.save(user);
        }

    }
}
