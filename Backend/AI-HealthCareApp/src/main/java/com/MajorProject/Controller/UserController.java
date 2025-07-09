package com.MajorProject.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.MajorProject.Repository.UserRepository;
import com.MajorProject.model.User;

@RestController
@CrossOrigin( origins = "http://localhost:4200")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	//constructor for user repository
	  public UserController(UserRepository userRepository) {
	        this.userRepository = userRepository;
	    }

	  @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody User user) {
		  
//          System.out.println(user.getUsername());  just checking if data is being received
//          System.out.println(user.getPassword());
          
          if (user.getUsername().equals("admin") && user.getPassword().equals("admin")) {
              return ResponseEntity.ok("admin");
          }
          
	        // Find user by username (or email)
	        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
	        
	        if (existingUser.isPresent()) {
	            User foundUser = existingUser.get();
//	            System.out.println(foundUser.getRole());  just checking if data is being received
	            
	            // Check if the passwords match
	            if (foundUser.getPassword().equals(user.getPassword())) {
	                // Depending on the role, return appropriate response
	                  if (foundUser.getRole().equals("Patient")) {
	                	  long uid = foundUser.getId();
	                    return ResponseEntity.ok("patient"+uid);
	                } else if (foundUser.getRole().equals("Doctor")) {
	                	      long uid = foundUser.getId();
//	                	      System.out.println(foundUser.getId());
	                    return ResponseEntity.ok("doctor"+uid);
	                }
	            } else {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
	            }
	        }
	        
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username");
	    }
	
	@PostMapping("/register")
	public User registerUser(@RequestBody User user) {
		return userRepository.save(user);
	}
	
}
