package com.MajorProject.Controller;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.MajorProject.Repository.UserRepository;
import com.MajorProject.Service.DoctorService;
import com.MajorProject.dto.DoctorDTO;
import com.MajorProject.model.Doctor;
import com.MajorProject.model.DoctorAvailability;
import com.MajorProject.model.User;

@RestController
@RequestMapping("/Doctor")
public class DoctorController {

	@Autowired
	DoctorService doctorService;
	
	@Autowired
	private UserRepository userRepository;

@PostMapping("/addprofile")
public ResponseEntity<String> AddDoctorProfile(
        @RequestParam("id") long id,
        @RequestParam String name,
        @RequestParam String about,
        @RequestParam("mobileNo") long mobileNo,
        @RequestParam String gender,
        @RequestParam int age,
        @RequestParam String city,
        @RequestParam MultipartFile image,
        @RequestParam String speciality,
        @RequestParam String experience,
        @RequestParam String clinicName,
        @RequestParam String clinicAddress,
        @RequestParam double consultationFees) throws IOException {

    // ✅ Step 1: Check if doctor profile already exists
    if (doctorService.checkprofileExistsOrNot(id)) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Doctor profile already exists for ID: " + id);
    }

    // ✅ Step 2: Fetch the User by ID
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found for ID: " + id));

    // ✅ Step 3: Create and populate Doctor entity
    Doctor doctor = new Doctor();
    doctor.setId(id); // Must match user.id
    doctor.setUser(user); // 🔗 Link to user

    doctor.setName(name);
    doctor.setAbout(about);
    doctor.setMobileNo(mobileNo);
    doctor.setAge(age);
    doctor.setGender(gender);
    doctor.setCity(city);
    doctor.setImage(image.getBytes()); // convert image to byte[]
    doctor.setExperience(experience);
    doctor.setSpeciality(speciality);
    doctor.setClinicName(clinicName);
    doctor.setClinicAddress(clinicAddress);
    doctor.setConsultationFees(consultationFees);

    // ✅ Step 4: Save doctor profile
    doctorService.saveProfile(doctor);

    return ResponseEntity.ok("Doctor profile saved successfully");
}

	@GetMapping("profile/{id}")
	public ResponseEntity<Map<String, Object>> showprofile(@PathVariable long id) {
		System.out.println(id);
		Optional<Doctor> doc = doctorService.FindDoctorById(id);
		if (doc.isPresent()) {
			Doctor doctor = doc.get();
			Map<String, Object> response = new HashMap<>();
			response.put("id", doctor.getId());
			response.put("name", doctor.getName());
			response.put("about", doctor.getAbout());
			response.put("gender", doctor.getGender());
			response.put("age", doctor.getAge());
			response.put("city", doctor.getCity());
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
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateDoctor(@PathVariable Long id, @RequestParam("name") String name,
			@RequestParam("about") String about, @RequestParam("mobileNo") long mobileNo,
			@RequestParam("gender") String gender, @RequestParam("age") int age, @RequestParam("city") String city,
			@RequestParam("speciality") String speciality, @RequestParam("experience") String experience,
			@RequestParam("clinicName") String clinicName, @RequestParam("clinicAddress") String clinicAddress,
			@RequestParam("consultationFees") int consultationFees,
			@RequestParam(value = "image", required = false) MultipartFile imageFile) {
		Optional<Doctor> doctorOptional = doctorService.FindDoctorById(id);
		if (!doctorOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
		}

		Doctor doctor = doctorOptional.get();
		doctor.setName(name);
		doctor.setAbout(about);
		doctor.setMobileNo(mobileNo);
		doctor.setGender(gender);
		doctor.setAge(age);
		doctor.setCity(city);
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
	public ResponseEntity<?> findDoctors(
	        @RequestParam(required = false) String name,
	        @RequestParam(required = false) String city,
	        @RequestParam(required = false) String speciality) {
	    List<DoctorDTO> doctors = doctorService.searchDoctors(name, city, speciality);
	    if (doctors.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No doctors found for the given criteria.");
	    }

	    return ResponseEntity.ok(doctors);
	}


	@PostMapping("/schedule/{doctorId}")
	public String addAvailability(@PathVariable("doctorId") long doctorId,
			@RequestBody DoctorAvailability availability) {
		System.out.println(availability);
		Doctor doctor = doctorService.FindDoctorById(doctorId)
				.orElseThrow(() -> new RuntimeException("Doctor not found with id : " + doctorId));

		DoctorAvailability avail = new DoctorAvailability();
		avail.setDate(availability.getDate());
		avail.setTime(availability.getTime());
		avail.setBooked(false);
		avail.setDoctor(doctor); // or load doctor using doctorId

		DoctorAvailability savedavail = doctorService.saveAvailability(avail);
//        System.out.println(savedavail);
		return "availability added";
	}

	@GetMapping("/schedule/find/{doctorId}")
	public ResponseEntity<?> FindAllAvailabilityByDoctorid(@PathVariable long doctorId) {

		List<DoctorAvailability> availList = doctorService.FindAllAvailabilities(doctorId);
		System.out.println(availList);
		return ResponseEntity.ok(availList);
	}

	@DeleteMapping("/schedule/delete/{availabilityId}")
	public String DeleteAvailablilityById(@PathVariable long availabilityId) {
		doctorService.deleteAvailability(availabilityId);
		return "availability deleted";
	}
}
