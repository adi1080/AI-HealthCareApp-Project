package com.MajorProject.Controller;

import com.MajorProject.Repository.UserRepository;
import com.MajorProject.Service.AIAnalysis;
import com.MajorProject.Service.DoctorService;
import com.MajorProject.Service.PatientService;
import com.MajorProject.Domain.FeedbackDTO;
import com.MajorProject.Domain.PatientDTO;
import com.MajorProject.Entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("Patient")
//@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {

	@Autowired
	PatientService ps;
	
	@Autowired
	DoctorService ds;

	@Autowired
	private UserRepository userRepository;

    @Autowired
    private AIAnalysis aiAnalysis;

    @PostMapping("/addProfile")
    public ResponseEntity<?> createProfile(
            @RequestParam("patient") String patientJson,
            @RequestParam(value = "report", required = false) MultipartFile reportFile) {

        ObjectMapper mapper = new ObjectMapper();
        Patient patient;

        try {
            patient = mapper.readValue(patientJson, Patient.class);  // JSON to Patient
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid patient data: " + e.getMessage());
        }

        // Handle file
        if (reportFile != null && !reportFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + reportFile.getOriginalFilename();

                // ✅ Absolute path instead of relative one
                String uploadDir = "F:/Pendrive Stuff/Major-Project/reports/";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                reportFile.transferTo(filePath.toFile());


                patient.setReportFilePath(fileName);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving file: " + e.getMessage());
            }
        }

        Optional<User> optionalUser = userRepository.findById(patient.getId());

        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + patient.getId());
        }

        // Check if profile exists
        if (ps.checkprofileExistsOrNot(patient.getId())) {
            return ResponseEntity.ok("Profile already exists");
        }

        patient.setUser(optionalUser.get());  // set the relationship

        ps.saveProfile(patient);

        return ResponseEntity.ok("Profile added successfully");
    }

    @GetMapping("/FindById/{id}")
	public ResponseEntity<PatientDTO> showProfileDetails(@PathVariable long id) {
	    Optional<Patient> optionalPatient = ps.FindById(id);

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();

            // 🚀 Run AI analysis in background if not already done
            if (Boolean.FALSE.equals(patient.getAiAnalysisDone())) {
                aiAnalysis.analyzeInBackground(patient);  // ✅ clean and threaded
            }

            // ✅ Return patient data immediately (without waiting)
            PatientDTO dto = ps.convertToDTO(patient);
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
    public ResponseEntity<?> addFeedback(@RequestBody Feedback fb) {
        System.out.println(fb);

        Doctor doctor = ds.FindDoctorById(fb.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Patient patient = ps.FindById(fb.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Check if feedback already exists for this doctor-patient pair
        Optional<Feedback> existingFeedbackOpt = ps.findFeedbackByDoctor_IdAndPatient_Id(doctor.getId(), patient.getId());

        Feedback feedbackToSave;
        if (existingFeedbackOpt.isPresent()) {
            // Update existing feedback
            feedbackToSave = existingFeedbackOpt.get();
            feedbackToSave.setRating(fb.getRating());
            feedbackToSave.setFeedbackComment(fb.getFeedbackComment());
            feedbackToSave.setDate(LocalDate.now());
        } else {
            // Create new feedback
            feedbackToSave = new Feedback();
            feedbackToSave.setRating(fb.getRating());
            feedbackToSave.setFeedbackComment(fb.getFeedbackComment());
            feedbackToSave.setDoctor(doctor);
            feedbackToSave.setPatient(patient);
            feedbackToSave.setDate(LocalDate.now());
        }

        ps.addFeedback(feedbackToSave); // This method should be able to save/update depending on whether ID is present

        return ResponseEntity.ok("Feedback saved successfully!");
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

    @GetMapping("/download-report/{filename:.+}")
    public ResponseEntity<Resource> downloadReport(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("F:/Pendrive Stuff/Major-Project/reports/").resolve(filename).normalize();
            UrlResource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body((Resource) resource);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
