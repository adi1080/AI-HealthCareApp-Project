package com.MajorProject.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
			ps.addProfile(patient);
			return "profile added Successfully";
		}
	}

	@GetMapping("FindById/{id}")
	public Optional<Patient> showProfileDetails(@PathVariable long id) {
        System.out.println(id);
		Optional<Patient> patient = ps.FindById(id);
		return patient;
	}

}
