package com.example.demo.authentication;

import com.example.demo.entity.User;

/**
 * Public API for authentication operations.
 * This interface defines the contract for authentication services.
 */
public interface AuthenticationService {
    
    /**
     * Register a new user.
     * 
     * @param user the user to register
     * @return the registered user
     * @throws IllegalArgumentException if email already exists
     */
    User registerUser(User user);
    
    /**
     * Check if a user exists with the given email.
     * 
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean userExists(String email);
    
    /**
     * Find a user by email.
     * 
     * @param email the email to search for
     * @return the user if found, null otherwise
     */
    User findByEmail(String email);
}



