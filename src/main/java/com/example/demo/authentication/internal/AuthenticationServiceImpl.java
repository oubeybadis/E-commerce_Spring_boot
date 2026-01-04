package com.example.demo.authentication.internal;

import com.example.demo.authentication.AuthenticationService;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Internal implementation of AuthenticationService.
 * This is part of the authentication module's internal implementation.
 */
@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public User registerUser(User user) {
        if (userExists(user.getEmail())) {
            throw new IllegalArgumentException("البريد الإلكتروني مستخدم بالفعل");
        }
        
        // Encode password if not already encoded
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // Set default role if not set
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("user");
        }
        
        return userRepository.save(user);
    }
    
    @Override
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}



