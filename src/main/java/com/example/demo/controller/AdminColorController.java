package com.example.demo.controller;

import com.example.demo.entity.Color;
import com.example.demo.entity.User;
import com.example.demo.service.ColorService;
import com.example.demo.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/products/colors")
public class AdminColorController {
    
    @Autowired
    private ColorService colorService;
    
    @Autowired
    private ThemeService themeService;
    
    @GetMapping
    public String index(Model model, @AuthenticationPrincipal User user) {
        List<Color> colors = colorService.findAllOrderByCreatedAtDesc();
        model.addAttribute("colors", colors);
        model.addAttribute("user", user);
        model.addAttribute("themeSetting", themeService.getDefaultTheme());
        model.addAttribute("title", "الألوان");
        return "admin/products/colors/index";
    }
    
    @GetMapping("/create")
    public String create(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("color", new Color());
        model.addAttribute("user", user);
        model.addAttribute("themeSetting", themeService.getDefaultTheme());
        model.addAttribute("title", "أضف لونًا جديدًا");
        return "admin/products/colors/create";
    }
    
    @PostMapping("/create")
    public String store(@RequestParam("name") String name,
                        @RequestParam("hex_code") String hexCode,
                        RedirectAttributes redirectAttributes,
                        @AuthenticationPrincipal User user) {
        if (name == null || name.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "اسم اللون مطلوب");
            return "redirect:/admin/products/colors/create";
        }
        
        if (hexCode == null || hexCode.trim().isEmpty() || !hexCode.matches("^#[0-9A-Fa-f]{6}$")) {
            redirectAttributes.addFlashAttribute("error", "كود اللون غير صحيح (يجب أن يكون بصيغة #RRGGBB)");
            return "redirect:/admin/products/colors/create";
        }
        
        if (colorService.existsByName(name.trim())) {
            redirectAttributes.addFlashAttribute("error", "اسم اللون موجود بالفعل");
            return "redirect:/admin/products/colors/create";
        }
        
        Color color = new Color();
        color.setName(name.trim());
        color.setHexCode(hexCode.trim());
        colorService.save(color);
        
        redirectAttributes.addFlashAttribute("success", "تم إنشاء اللون بنجاح!");
        return "redirect:/admin/products/colors";
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, 
                      @AuthenticationPrincipal User user) {
        Optional<Color> colorOpt = colorService.findById(id);
        if (colorOpt.isPresent()) {
            model.addAttribute("color", colorOpt.get());
            model.addAttribute("user", user);
            model.addAttribute("themeSetting", themeService.getDefaultTheme());
            model.addAttribute("title", "تعديل اللون");
            return "admin/products/colors/edit";
        }
        return "redirect:/admin/products/colors";
    }
    
    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                        @RequestParam("name") String name,
                        @RequestParam("hex_code") String hexCode,
                        RedirectAttributes redirectAttributes,
                        @AuthenticationPrincipal User user) {
        Optional<Color> colorOpt = colorService.findById(id);
        if (colorOpt.isPresent()) {
            Color color = colorOpt.get();
            if (name == null || name.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "اسم اللون مطلوب");
                return "redirect:/admin/products/colors/" + id + "/edit";
            }
            
            if (hexCode == null || hexCode.trim().isEmpty() || !hexCode.matches("^#[0-9A-Fa-f]{6}$")) {
                redirectAttributes.addFlashAttribute("error", "كود اللون غير صحيح");
                return "redirect:/admin/products/colors/" + id + "/edit";
            }
            
            if (colorService.existsByNameAndIdNot(name.trim(), id)) {
                redirectAttributes.addFlashAttribute("error", "اسم اللون موجود بالفعل");
                return "redirect:/admin/products/colors/" + id + "/edit";
            }
            
            color.setName(name.trim());
            color.setHexCode(hexCode.trim());
            colorService.save(color);
            redirectAttributes.addFlashAttribute("success", "تم تحديث اللون بنجاح!");
            return "redirect:/admin/products/colors";
        }
        return "redirect:/admin/products/colors";
    }
    
    @DeleteMapping("/{id}")
    public String destroy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        colorService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "تم حذف اللون بنجاح!");
        return "redirect:/admin/products/colors";
    }
}

