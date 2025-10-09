package com.MajorProject.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MajorProject.Repository.UserRepository;
import com.MajorProject.Security.JwtUtil;
import com.MajorProject.Service.DoctorService;
import com.MajorProject.Service.PatientService;
import com.MajorProject.Entity.User;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
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
            if (!foundUser.isPermitted()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your account has been blocked. Please contact admin.");
            }

            // ✅ Verify password
            if (foundUser.getPassword().equals(user.getPassword())) {
                String token = jwtUtil.generateToken(foundUser);

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("id", foundUser.getId());
                response.put("role", foundUser.getRole());
                response.put("permitted", foundUser.isPermitted());

                System.out.println("response being sent : " + response);
                return ResponseEntity.ok(response);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists with this email.");
        }
        user.setPermitted(true);

        //user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/permit/{id}")
    public ResponseEntity<String> permitDoctor(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setPermitted(true);
            userRepository.save(user.get());
            return ResponseEntity.ok("User blocked.");
        }
        return ResponseEntity.ok("User permitted.");
    }

    @PostMapping("/block/{id}")
    public ResponseEntity<String> blockDoctor(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setPermitted(false);
            userRepository.save(user.get());
            return ResponseEntity.ok("User blocked.");
        }
        return ResponseEntity.ok("User blocked.");
    }
}
	

