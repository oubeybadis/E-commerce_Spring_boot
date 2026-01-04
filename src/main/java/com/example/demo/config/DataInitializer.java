package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ThemeRepository themeRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Create default admin user if not exists
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("admin");
            admin.setStart(LocalDate.now());
            admin.setEnd(LocalDate.now().plusMonths(1));
            userRepository.save(admin);
            System.out.println("Default admin user created: admin@example.com / admin123");
        }
        
        // Create default theme if not exists
        if (themeRepository.findById(1L).isEmpty()) {
            Theme theme = new Theme();
            theme.setPrimaryColor("#03aec9");
            theme.setLogo("imgs/logo.png");
            theme.setLanding("imgs/landing.jpg");
            themeRepository.save(theme);
            System.out.println("Default theme created");
        }
        
        // Create default category if not exists
        if (categoryRepository.count() == 0) {
            Category category = new Category();
            category.setName("Default Category");
            categoryRepository.save(category);
            System.out.println("Default category created");
        }
    }
}

