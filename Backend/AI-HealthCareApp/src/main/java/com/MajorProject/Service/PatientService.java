package com.MajorProject.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MajorProject.Repository.AppointmentRepository;
import com.MajorProject.Repository.DoctorRepository;
import com.MajorProject.Repository.FeedbackRepository;
import com.MajorProject.Repository.PatientRepository;
import com.MajorProject.dto.AppointmentDTO;
import com.MajorProject.dto.DoctorDTO;
import com.MajorProject.dto.FeedbackDTO;
import com.MajorProject.dto.PatientDTO;
import com.MajorProject.model.Appointment;
import com.MajorProject.model.Doctor;
import com.MajorProject.model.DoctorAvailability;
import com.MajorProject.model.Feedback;
import com.MajorProject.model.Patient;

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
	    dto.setName(patient.getName()); // capitalized field, but getter is correct
	    dto.setAge(patient.getAge());
	    dto.setGender(patient.getGender());
	    dto.setAddress(patient.getAddress());
	    dto.setMobileno(patient.getMobileno());
	    dto.setHistory(patient.getHistory());

	    List<AppointmentDTO> appointmentDTOs = new ArrayList<>();
	    if (patient.getAppointments() != null) {
	        for (Appointment appointment : patient.getAppointments()) {
	            AppointmentDTO aDto = new AppointmentDTO();
	            aDto.setAppointmentId(appointment.getAppointmentId());
	            aDto.setDoctorId(appointment.getDoctor() != null ? appointment.getDoctor().getId() : null);
	            aDto.setAvailabilityId(appointment.getAvailability() != null ? appointment.getAvailability().getId() : null);
	            aDto.setReason(appointment.getReason());
	            aDto.setStatus(appointment.getStatus() != null ? appointment.getStatus().getStatus() : null);
	            appointmentDTOs.add(aDto);
	        }
	    }

	    dto.setAppointments(appointmentDTOs);
	    return dto;
	}


}
