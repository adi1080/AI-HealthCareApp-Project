package com.MajorProject.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MajorProject.Repository.AppointmentRepository;
import com.MajorProject.Repository.PatientRepository;
import com.MajorProject.model.Appointment;
import com.MajorProject.model.DoctorAvailability;
import com.MajorProject.model.Patient;

@Service
public class PatientService {

	@Autowired
	PatientRepository pr;
	
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
}
