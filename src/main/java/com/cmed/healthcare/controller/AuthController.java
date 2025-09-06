package com.cmed.healthcare.controller;

import com.cmed.healthcare.model.user;
import com.cmed.healthcare.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // //Signup
    // @PostMapping("/signup")
    // public ResponseEntity<user> signup(@RequestBody user user) {
    //     user.setPassword(passwordEncoder.encode(user.getPassword())); // encode password
    //     return ResponseEntity.ok(userRepo.save(user));
    // }

@PostMapping("/signup")
public ResponseEntity<?> signup(@RequestBody user user) {

     // Check if username already exists
    if (userRepo.existsByUsername(user.getUsername())) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error: Username already exists!");
    }
    
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    // enabled=false for all signups (control to make it true to DB)
    user.setEnabled(false);

    // Validate role: only allowed roles can be selected
    List<String> allowedRoles = List.of("USER", "DOCTOR", "PHARMACIST", "MEDICAL_STAFF");
    if (!allowedRoles.contains(user.getRole())) {
        user.setRole("USER"); 
    }
    user saved = userRepo.save(user);

    // store all
    Map<String, Object> response = new HashMap<>();
    response.put("id", saved.getId());
    response.put("username", saved.getUsername());
    response.put("role", saved.getRole());
    response.put("enabled", saved.isEnabled());

    return ResponseEntity.ok(response);
}


@PutMapping("/approve/{id}")
public ResponseEntity<String> approveUser(@PathVariable Long id) {
    user user = userRepo.findById(id).orElseThrow();
    user.setEnabled(true);
    userRepo.save(user);
    return ResponseEntity.ok("User approved successfully!");
}




    // // Test endpoint (secured)
    // @GetMapping("/me")
    // public ResponseEntity<String> me(@RequestParam String username) {
    //     return ResponseEntity.ok("Hello " + username + "!");
    // }
}
