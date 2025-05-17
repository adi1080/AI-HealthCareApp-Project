package com.MajorProject.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.MajorProject.Repository.PatientRepository;
import com.MajorProject.Service.PatientService;
import com.MajorProject.model.Patient;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("Patient")
@CrossOrigin(origins = "http://localhost:4200/patient")
public class PatientController {

	@Autowired
	PatientService ps;

	@PostMapping("addProfile")
	public String createProfile(@RequestBody Patient patient) {
		System.out.println(patient);
		boolean exist = ps.checkprofileExistsOrNot(patient.getId());
		if (exist == true) {
			System.out.println("profile already exists");
			return "profile already exists";
		} else {
			ps.saveProfile(patient);
			return "profile added Successfully";
		}
	}

	@GetMapping("FindById/{id}")
	public Optional<Patient> showProfileDetails(@PathVariable long id) {
        System.out.println(id);
		Optional<Patient> patient = ps.FindById(id);
		return patient;
	}

	@PutMapping("/updateprofile/{id}")
	public ResponseEntity<?> updateprofile(@PathVariable long id , @RequestBody Patient incomingDataPatient){
		 Optional<Patient> existingPatientOpt = ps.FindById(id);
		 
		if(!existingPatientOpt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("patient not found");
		}
		
		Patient existingPatient = existingPatientOpt.get();
		existingPatient.setName(incomingDataPatient.getName());
		existingPatient.setAge(incomingDataPatient.getAge());
		existingPatient.setGender(incomingDataPatient.getGender());
		existingPatient.setMobileno(incomingDataPatient.getMobileno());
		existingPatient.setAddress(incomingDataPatient.getAddress());
		existingPatient.setHistory(incomingDataPatient.getHistory());
		
		ps.saveProfile(existingPatient);
				
		return ResponseEntity.ok(existingPatient);
	}
}
