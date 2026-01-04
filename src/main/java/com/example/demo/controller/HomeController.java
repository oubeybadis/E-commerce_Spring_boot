package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ThemeService themeService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("title", "Ecommerce Store");
        model.addAttribute("themeSetting", themeService.getDefaultTheme());
        model.addAttribute("products", productService.findAllWithRelations());
        model.addAttribute("user", user);
        return "pages/home";
    }
}

