package com.MajorProject.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.MajorProject.Repository.UserRepository;
import com.MajorProject.Security.JwtUtil;
import com.MajorProject.Service.DoctorService;
import com.MajorProject.Service.PatientService;
import com.MajorProject.model.Doctor;
import com.MajorProject.model.Patient;
import com.MajorProject.model.User;

@RestController
@CrossOrigin( origins = "http://localhost:4200")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DoctorService ds;
	
	@Autowired
	private PatientService ps;
	
	@Autowired
	private JwtUtil jwtUtil;

	
	//constructor for user repository
	  public UserController(UserRepository userRepository) {
	        this.userRepository = userRepository;
	    }

	  @PostMapping("/login")
	  public ResponseEntity<?> login(@RequestBody User user) {
	      Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

	      if (existingUser.isPresent()) {
	          User foundUser = existingUser.get();
	          if (foundUser.getPassword().equals(user.getPassword())) {
	              String token = jwtUtil.generateToken(foundUser);

	              Map<String, Object> response = new HashMap<>();
	              response.put("token", token);
	              response.put("id", foundUser.getId());
	              response.put("role", foundUser.getRole());

                  System.out.println("response being sent : "+response);
	              return ResponseEntity.ok(response);
	          }
	          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
	      }

	      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username");
	  }
	
		@PostMapping("/register")
		public ResponseEntity<?> registerUser(@RequestBody User user) {
		    User savedUser = userRepository.save(user);
		    return ResponseEntity.ok(savedUser);
		}

	}
	

