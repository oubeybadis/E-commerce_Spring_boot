package com.example.demo.controller;

import com.example.demo.entity.Size;
import com.example.demo.entity.User;
import com.example.demo.service.SizeService;
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
@RequestMapping("/admin/products/sizes")
public class AdminSizeController {
    
    @Autowired
    private SizeService sizeService;
    
    @Autowired
    private ThemeService themeService;
    
    @GetMapping
    public String index(Model model, @AuthenticationPrincipal User user) {
        List<Size> sizes = sizeService.findAllOrderByCreatedAtDesc();
        model.addAttribute("sizes", sizes);
        model.addAttribute("user", user);
        model.addAttribute("themeSetting", themeService.getDefaultTheme());
        model.addAttribute("title", "الأحجام");
        return "admin/products/sizes/index";
    }
    
    @GetMapping("/create")
    public String create(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("size", new Size());
        model.addAttribute("user", user);
        model.addAttribute("themeSetting", themeService.getDefaultTheme());
        model.addAttribute("title", "أضف حجمًا جديدًا");
        return "admin/products/sizes/create";
    }
    
    @PostMapping("/create")
    public String store(@RequestParam("name") String name,
                        RedirectAttributes redirectAttributes,
                        @AuthenticationPrincipal User user) {
        if (name == null || name.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "اسم الحجم مطلوب");
            return "redirect:/admin/products/sizes/create";
        }
        
        if (sizeService.existsByName(name.trim())) {
            redirectAttributes.addFlashAttribute("error", "اسم الحجم موجود بالفعل");
            return "redirect:/admin/products/sizes/create";
        }
        
        Size size = new Size();
        size.setName(name.trim());
        sizeService.save(size);
        
        redirectAttributes.addFlashAttribute("success", "تم إنشاء الحجم بنجاح!");
        return "redirect:/admin/products/sizes";
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, 
                      @AuthenticationPrincipal User user) {
        Optional<Size> sizeOpt = sizeService.findById(id);
        if (sizeOpt.isPresent()) {
            model.addAttribute("size", sizeOpt.get());
            model.addAttribute("user", user);
            model.addAttribute("themeSetting", themeService.getDefaultTheme());
            model.addAttribute("title", "تعديل الحجم");
            return "admin/products/sizes/edit";
        }
        return "redirect:/admin/products/sizes";
    }
    
    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                        @RequestParam("name") String name,
                        RedirectAttributes redirectAttributes,
                        @AuthenticationPrincipal User user) {
        Optional<Size> sizeOpt = sizeService.findById(id);
        if (sizeOpt.isPresent()) {
            Size size = sizeOpt.get();
            if (name == null || name.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "اسم الحجم مطلوب");
                return "redirect:/admin/products/sizes/" + id + "/edit";
            }
            
            if (sizeService.existsByNameAndIdNot(name.trim(), id)) {
                redirectAttributes.addFlashAttribute("error", "اسم الحجم موجود بالفعل");
                return "redirect:/admin/products/sizes/" + id + "/edit";
            }
            
            size.setName(name.trim());
            sizeService.save(size);
            redirectAttributes.addFlashAttribute("success", "تم تحديث الحجم بنجاح!");
            return "redirect:/admin/products/sizes";
        }
        return "redirect:/admin/products/sizes";
    }
    
    @DeleteMapping("/{id}")
    public String destroy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        sizeService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "تم حذف الحجم بنجاح!");
        return "redirect:/admin/products/sizes";
    }
}

