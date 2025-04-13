package com.MajorProject.Controller;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.MajorProject.Service.DoctorService;
import com.MajorProject.model.Doctor;

@RestController
@CrossOrigin( origins = "http://localhost:4200/doctor")
@RequestMapping("/Doctor")
public class DoctorController {

	@Autowired
    DoctorService doctorService;

    @PostMapping("/addprofile")
    public String updateDoctorProfile(
    		@RequestParam("id") long id,
            @RequestParam("name") String name,
            @RequestParam("mobileNo") long mobileno,
            @RequestParam("gender") String gender,
            @RequestParam("age") int age,
            @RequestParam("image") MultipartFile image,
            @RequestParam("speciality") String speciality,
            @RequestParam("experience") String experience,
            @RequestParam("clinicName") String clinicName,
            @RequestParam("clinicAddress") String clinicAddress,
            @RequestParam("consultationFees") double consultationFees) throws IOException {

        // Convert the image file to a byte array (this will be stored as BLOB)
        byte[] imageBytes = image.getBytes();

        // Create a Doctor object and populate it with the incoming data
        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setName(name);
        doctor.setMobileNo(mobileno);
        doctor.setAge(age);
        doctor.setGender(gender);
        doctor.setImage(image.getBytes());
        doctor.setExperience(experience);
        doctor.setSpeciality(speciality);
        doctor.setClinicName(clinicName);
        doctor.setClinicAddress(clinicAddress);
        doctor.setConsultationFees(consultationFees);
        
        System.out.println(doctor);

        // Save the doctor profile
        doctorService.saveProfile(doctor);

        return "profile saved Successfully";
        
    }
    
//    @PostMapping("/addprofile")
//    public Doctor addDetails(@RequestBody Doctor doctor) {
//    	System.out.println(doctor);
//    	return doctorService.saveProfile(doctor);
//    }
}
