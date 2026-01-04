package com.example.demo.controller;

import com.example.demo.entity.Theme;
import com.example.demo.entity.User;
import com.example.demo.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/admin/themes")
public class AdminThemeController {
    
    @Autowired
    private ThemeService themeService;
    
    @Value("${file.upload-dir:storage}")
    private String uploadDir;
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        Theme theme = themeService.getDefaultTheme();
        model.addAttribute("themeSetting", theme);
        model.addAttribute("user", user);
        model.addAttribute("title", "إعدادات المتجر");
        return "admin/themes/edit";
    }
    
    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                        @RequestParam(value = "logo", required = false) MultipartFile logo,
                        @RequestParam(value = "landing", required = false) MultipartFile landing,
                        @RequestParam("primary_color") String primaryColor,
                        RedirectAttributes redirectAttributes,
                        @AuthenticationPrincipal User user) {
        
        try {
            Theme theme = themeService.getDefaultTheme();
            
            // Handle logo upload
            if (logo != null && !logo.isEmpty() && logo.getSize() > 0) {
                // Delete old logo if exists
                if (theme.getLogo() != null && !theme.getLogo().isEmpty()) {
                    deleteImageFile(theme.getLogo());
                }
                String logoPath = saveImage(logo, "logos");
                theme.setLogo(logoPath);
            }
            
            // Handle landing image upload
            if (landing != null && !landing.isEmpty() && landing.getSize() > 0) {
                // Delete old landing image if exists
                if (theme.getLanding() != null && !theme.getLanding().isEmpty()) {
                    deleteImageFile(theme.getLanding());
                }
                String landingPath = saveImage(landing, "landings");
                theme.setLanding(landingPath);
            }
            
            // Update primary color
            theme.setPrimaryColor(primaryColor);
            
            themeService.save(theme);
            redirectAttributes.addFlashAttribute("success", "تم تحديث إعدادات السمة بنجاح!");
            return "redirect:/admin/themes/" + id + "/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "حدث خطأ: " + e.getMessage());
            return "redirect:/admin/themes/" + id + "/edit";
        }
    }
    
    private String saveImage(MultipartFile file, String subfolder) throws IOException {
        Path uploadPath = Paths.get(uploadDir, subfolder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return subfolder + "/" + fileName;
    }
    
    private void deleteImageFile(String imageUrl) {
        try {
            Path filePath = Paths.get(uploadDir, imageUrl);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log error but don't fail
            System.err.println("Failed to delete image: " + imageUrl);
        }
    }
}

