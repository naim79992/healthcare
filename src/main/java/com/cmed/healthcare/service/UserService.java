package com.cmed.healthcare.service;

import com.cmed.healthcare.model.user;
import com.cmed.healthcare.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public user createUser(String username, String rawPassword) {
        user user = new user();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword)); // encode password
        user.setRole("USER");
        return userRepo.save(user);
    }

}
