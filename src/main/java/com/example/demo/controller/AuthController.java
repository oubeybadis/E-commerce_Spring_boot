package com.example.demo.controller;

import com.example.demo.authentication.AuthenticationService;
import com.example.demo.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @GetMapping("/login")
    public String loginPage(Model model, @AuthenticationPrincipal User user, 
                           @RequestParam(required = false) String error) {
        System.out.println(user);
        if (user != null) {
            return "redirect:/";
        }
        model.addAttribute("title", "تسجيل الدخول");
        if (error != null) {
            model.addAttribute("error", "البريد الإلكتروني أو كلمة المرور غير صحيحة");
        }
        return "auth/login";
    }
    
    // Note: POST /login is handled by Spring Security formLogin configuration
    // No need for manual authentication - Spring Security handles it automatically
    
    @GetMapping("/register")
    public String registerPage(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            return "redirect:/";
        }
        model.addAttribute("title", "تسجيل");
        model.addAttribute("user", new User());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute User user, BindingResult result, Model model,
                          @AuthenticationPrincipal User currentUser) {
        if (currentUser != null) {
            return "redirect:/";
        }
        
        if (result.hasErrors()) {
            model.addAttribute("title", "تسجيل");
            return "auth/register";
        }
        
        // Validate required fields
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            model.addAttribute("nameError", "الاسم مطلوب");
            model.addAttribute("title", "تسجيل");
            return "auth/register";
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            model.addAttribute("emailError", "البريد الإلكتروني مطلوب");
            model.addAttribute("title", "تسجيل");
            return "auth/register";
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            model.addAttribute("passwordError", "كلمة المرور مطلوبة");
            model.addAttribute("title", "تسجيل");
            return "auth/register";
        }
        
        // Check if user already exists
        if (authenticationService.userExists(user.getEmail())) {
            model.addAttribute("emailError", "البريد الإلكتروني مستخدم بالفعل");
            model.addAttribute("title", "تسجيل");
            return "auth/register";
        }
        
        // Register user using authentication module API
        try {
            authenticationService.registerUser(user);
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("emailError", e.getMessage());
            model.addAttribute("title", "تسجيل");
            return "auth/register";
        }
    }
}

