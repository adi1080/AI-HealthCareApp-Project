package com.MajorProject.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MajorProject.Repository.AppointmentRepository;
import com.MajorProject.Repository.DoctorAvailabilityRepository;
import com.MajorProject.Repository.DoctorRepository;
import com.MajorProject.Repository.FeedbackRepository;
import com.MajorProject.Repository.PatientRepository;
import com.MajorProject.Domain.AppointmentDTO;
import com.MajorProject.Domain.DoctorDTO;
import com.MajorProject.Domain.FeedbackDTO;
import com.MajorProject.Domain.PatientDTO;
import com.MajorProject.Entity.Appointment;
import com.MajorProject.Entity.Doctor;
import com.MajorProject.Entity.DoctorAvailability;
import com.MajorProject.Entity.Feedback;
import com.MajorProject.Entity.Patient;

@Service
public class PatientService {

	@Autowired
	PatientRepository pr;
	
	@Autowired
	FeedbackRepository fb;
	
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
	    	if(pr.existsById(id)) {
	    		return true;
	    	}
	    	else {
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

}
