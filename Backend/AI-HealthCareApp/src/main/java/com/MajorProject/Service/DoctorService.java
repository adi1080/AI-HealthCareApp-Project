package com.MajorProject.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.MajorProject.Repository.DoctorAvailabilityRepository;
import com.MajorProject.Repository.DoctorRepository;
import com.MajorProject.model.Doctor;
import com.MajorProject.model.DoctorAvailability;

@Service
public class DoctorService {

	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired DoctorAvailabilityRepository availabilityRepo;

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

	public List<Doctor> find(String city , String speciality) {
		System.out.println("Finding doctors with speciality: '" + speciality + "' and address: '" + city + "'");
		return doctorRepository.findAllByCityIgnoreCaseAndSpecialityIgnoreCase(city , speciality);
	}

	public List<Doctor> findbyname(String name) {
		System.out.println("Finding doctor by name : "+name);
		return doctorRepository.findByNameContainingIgnoreCase(name);
	}
	
	public DoctorAvailability saveAvailability(DoctorAvailability availability) {
		return availabilityRepo.save(availability);
	}

	public List<DoctorAvailability> FindAllAvailabilities(long doctorId) {
		
		return availabilityRepo.findByDoctor_Id(doctorId);
	}

	public void deleteAvailability(long availabilityId) {
		 availabilityRepo.deleteById(availabilityId);
	}
}
