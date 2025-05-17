package com.MajorProject.Controller;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public String AddDoctorProfile(
    		@RequestParam("id") long id,
            @RequestParam String name,
            @RequestParam("mobileNo") long mobileno, //matchinig values of angular needed in () if u give different names here
            @RequestParam String gender,
            @RequestParam int age,
            @RequestParam MultipartFile image,
            @RequestParam String speciality,
            @RequestParam String experience,
            @RequestParam String clinicName,
            @RequestParam String clinicAddress,
            @RequestParam double consultationFees) throws IOException {
    	
    	boolean exist = doctorService.checkprofileExistsOrNot(id);
    	if(exist == true) {
    		System.out.println("profile already exists");
    		return "profile already exists";
    	}
    	else {
        // Create a Doctor object and populate it with the incoming data
        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setName(name);
        doctor.setMobileNo(mobileno);
        doctor.setAge(age);
        doctor.setGender(gender);
        doctor.setImage(image.getBytes()); //convert image file to byte array (stored as blob in db)
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
    }
    
    @GetMapping("profile/{id}")
    public ResponseEntity<Map<String, Object>> showprofile(@PathVariable long id)
    {
    	System.out.println(id);
    	Optional<Doctor> doc = doctorService.FindDoctorById(id);
        if (doc.isPresent()) {
            Doctor doctor = doc.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", doctor.getId());
            response.put("name", doctor.getName());
            response.put("gender", doctor.getGender());
            response.put("age", doctor.getAge());
            response.put("mobileNo", doctor.getMobileNo());
            response.put("speciality", doctor.getSpeciality());
            response.put("experience", doctor.getExperience());
            response.put("clinicName", doctor.getClinicName());
            response.put("clinicAddress", doctor.getClinicAddress());
            response.put("consultationFees", doctor.getConsultationFees());
            
            // Convert byte[] to Base64
            String base64Image = Base64.getEncoder().encodeToString(doctor.getImage());
            response.put("image", base64Image);
            
            return ResponseEntity.ok(response);
        }
        else {
        	 return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable Long id,
                                          @RequestParam("name") String name,
                                          @RequestParam("mobileNo") long mobileNo,
                                          @RequestParam("gender") String gender,
                                          @RequestParam("age") int age,
                                          @RequestParam("speciality") String speciality,
                                          @RequestParam("experience") String experience,
                                          @RequestParam("clinicName") String clinicName,
                                          @RequestParam("clinicAddress") String clinicAddress,
                                          @RequestParam("consultationFees") int consultationFees,
                                          @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        Optional<Doctor> doctorOptional = doctorService.FindDoctorById(id);
        if (!doctorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        Doctor doctor = doctorOptional.get();
        doctor.setName(name);
        doctor.setMobileNo(mobileNo);
        doctor.setGender(gender);
        doctor.setAge(age);
        doctor.setSpeciality(speciality);
        doctor.setExperience(experience);
        doctor.setClinicName(clinicName);
        doctor.setClinicAddress(clinicAddress);
        doctor.setConsultationFees(consultationFees);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                doctor.setImage(imageFile.getBytes());
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image processing failed");
            }
        }

        doctorService.saveProfile(doctor);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/search")
    public ResponseEntity<Set<Doctor>> findadoctor(@RequestParam("clinicAddress") String address , @RequestParam("clinicName") String name){
    	System.out.println(name);
    	System.out.println(address);
    	Set<Doctor> doclist = doctorService.find(name , address);
    	return ResponseEntity.ok(doclist);
    }
}
