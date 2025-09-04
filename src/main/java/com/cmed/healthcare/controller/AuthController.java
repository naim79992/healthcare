package com.cmed.healthcare.controller;

import com.cmed.healthcare.model.user;
import com.cmed.healthcare.repository.UserRepository;
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
public ResponseEntity<String> signup(@RequestBody user user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setEnabled(false); // নতুন user disable
    userRepo.save(user);
    return ResponseEntity.ok("Signup successful! Please wait for approval.");
}

@PutMapping("/approve/{id}")
public ResponseEntity<String> approveUser(@PathVariable Long id) {
    user user = userRepo.findById(id).orElseThrow();
    user.setEnabled(true);
    userRepo.save(user);
    return ResponseEntity.ok("User approved successfully!");
}




    // Test endpoint (secured)
    @GetMapping("/me")
    public ResponseEntity<String> me(@RequestParam String username) {
        return ResponseEntity.ok("Hello " + username + "!");
    }
}
