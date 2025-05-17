package com.MajorProject.Service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.MajorProject.Repository.DoctorRepository;
import com.MajorProject.model.Doctor;

@Service
public class DoctorService {

	@Autowired
	private DoctorRepository doctorRepository;

	  // Find doctor by ID
    public Optional<Doctor> FindDoctorById(long id) {
        return doctorRepository.findById(id);
    }

    // Save or update doctor profile
    public Doctor saveProfile(Doctor doctor) {
        return doctorRepository.save(doctor); // This will either update or insert the doctor based on whether the ID exists
    }
	
    public boolean checkprofileExistsOrNot(long id) {
    	if(doctorRepository.existsById(id)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    	
    }

	public Set<Doctor> find(String name, String address) {
		return doctorRepository.findAllByClinicNameAndClinicAddress(name , address);
	}
}
