package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ThemeService themeService;
    
    @GetMapping("/products/{id}")
    public String showProduct(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        Product product = productService.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        model.addAttribute("product", product);
        model.addAttribute("themeSetting", themeService.getDefaultTheme());
        model.addAttribute("user", user);
        model.addAttribute("title", product.getName());
        return "clients/products/show";
    }
    
    @GetMapping("/client/products/{id}/show")
    public String showProductClient(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        // Redirect to the simpler URL
        return "redirect:/products/" + id;
    }
}

