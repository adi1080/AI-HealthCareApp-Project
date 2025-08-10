package com.MajorProject.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.MajorProject.Repository.PatientRepository;
import com.MajorProject.Repository.UserRepository;
import com.MajorProject.Service.DoctorService;
import com.MajorProject.Service.PatientService;
import com.MajorProject.dto.FeedbackDTO;
import com.MajorProject.dto.PatientDTO;
import com.MajorProject.model.Appointment;
import com.MajorProject.model.Doctor;
import com.MajorProject.model.DoctorAvailability;
import com.MajorProject.model.Feedback;
import com.MajorProject.model.Patient;
import com.MajorProject.model.User;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("Patient")
@CrossOrigin(origins = "http://localhost:4200/patient")
public class PatientController {

	@Autowired
	PatientService ps;
	
	@Autowired
	DoctorService ds;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("addProfile")
	public String createProfile(@RequestBody Patient patient) {
	    // patient.id is expected to be user.id
	    Optional<User> optionalUser = userRepository.findById(patient.getId());
	    if (!optionalUser.isPresent()) {
	        return "User not found with ID: " + patient.getId();
	    }

	    // Check if profile already exists
	    if (ps.checkprofileExistsOrNot(patient.getId())) {
	        return "Profile already exists";
	    }

	    // 🔗 Link user to patient
	    patient.setUser(optionalUser.get());

	    // 🛠 OPTIONAL: Set patient.id = user.id to maintain same identity
	    patient.setId(optionalUser.get().getId());

	    // ✅ Save properly linked patient
	    ps.saveProfile(patient);
	    return "Profile added successfully";
	}



	@GetMapping("FindById/{id}")
	public ResponseEntity<PatientDTO> showProfileDetails(@PathVariable long id) {
	    Optional<Patient> optionalPatient = ps.FindById(id);

	    if (optionalPatient.isPresent()) {
	        PatientDTO dto = ps.convertToDTO(optionalPatient.get());
	        return ResponseEntity.ok(dto);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
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
	
	@PostMapping("BookAppointment")
	public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
		System.out.println(appointment);
	    try {
	        // You receive doctor, patient, and availability as nested objects with only IDs
	        Doctor doctor = ds.FindDoctorById(appointment.getDoctor().getId())
	                .orElseThrow(() -> new RuntimeException("Doctor not found"));

	        Patient patient = ps.FindById(appointment.getPatient().getId())
	                .orElseThrow(() -> new RuntimeException("Patient not found"));

	        DoctorAvailability availability = ds.findAvailability(appointment.getAvailability().getId())
	                .orElseThrow(() -> new RuntimeException("Availability not found"));

	        if (ps.existsByAvailability(availability)) {
	            return ResponseEntity.badRequest().body("Slot already booked.");
	        }

	        appointment.setDoctor(doctor);
	        appointment.setPatient(patient);
	        appointment.setAvailability(availability);
	        appointment.setStatus(Appointment.AppointmentStatus.PENDING);
	        availability.setBooked(true);
	        
	        Appointment saved = ps.saveAppointment(appointment);
	        return ResponseEntity.ok(saved);

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}
	
	@PostMapping("add-feedback")
	public ResponseEntity<?> addFeedback(@RequestBody Feedback fb){
		System.out.println(fb);
        // You receive doctor, patient, and availability as nested objects with only IDs
        Doctor doctor = ds.FindDoctorById(fb.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Patient patient = ps.FindById(fb.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
		
		     Feedback addFb = new Feedback();
		     addFb.setRating(fb.getRating());
		     addFb.setFeedbackComment(fb.getFeedbackComment());
		     addFb.setDoctor(doctor);
		     addFb.setPatient(patient);
		     addFb.setDate(LocalDate.now());
		     
//		     System.out.println(addFb);
		     ps.addFeedback(addFb);
		return ResponseEntity.ok("saved!");
	}
	
	@GetMapping("getAllFeedbacks/{doctorId}")
	public ResponseEntity<List<FeedbackDTO>> getAllFeedbacks(@PathVariable long doctorId){
	    List<FeedbackDTO> list = ps.getFeedbacks(doctorId); 
	    return ResponseEntity.ok(list);
	}

	@DeleteMapping("deleteAppointment/{appointmentId}")
	public String cancelAppointment(@PathVariable("appointmentId") long id) {
		ps.deleteAppointment(id);
		return "Appointment Canceled!";
	}
}
