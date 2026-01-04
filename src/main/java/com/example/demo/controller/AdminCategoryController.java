package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.service.CategoryService;
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
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ThemeService themeService;
    
    @GetMapping
    public String index(Model model, @AuthenticationPrincipal User user) {
        List<Category> categories = categoryService.findAllOrderByCreatedAtDesc();
        model.addAttribute("categories", categories);
        model.addAttribute("user", user);
        model.addAttribute("themeSetting", themeService.getDefaultTheme());
        model.addAttribute("title", "الفئات");
        return "admin/categories/index";
    }
    
    @GetMapping("/create")
    public String create(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("category", new Category());
        model.addAttribute("user", user);
        model.addAttribute("themeSetting", themeService.getDefaultTheme());
        model.addAttribute("title", "أضف فئة جديدة");
        return "admin/categories/create";
    }
    
    @PostMapping
    public String store(@RequestParam("name") String name,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal User user) {
        if (name == null || name.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "اسم الفئة مطلوب");
            return "redirect:/admin/categories/create";
        }
        
        if (categoryService.existsByName(name.trim())) {
            redirectAttributes.addFlashAttribute("error", "اسم الفئة موجود بالفعل");
            return "redirect:/admin/categories/create";
        }
        
        Category category = new Category();
        category.setName(name.trim());
        categoryService.save(category);
        
        redirectAttributes.addFlashAttribute("success", "تم إنشاء الفئة بنجاح!");
        return "redirect:/admin/categories";
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, 
                      @AuthenticationPrincipal User user) {
        Optional<Category> categoryOpt = categoryService.findById(id);
        if (categoryOpt.isPresent()) {
            model.addAttribute("category", categoryOpt.get());
            model.addAttribute("user", user);
            model.addAttribute("themeSetting", themeService.getDefaultTheme());
            model.addAttribute("title", "تعديل الفئة");
            return "admin/categories/edit";
        }
        return "redirect:/admin/categories";
    }
    
    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                        @RequestParam("name") String name,
                        RedirectAttributes redirectAttributes,
                        @AuthenticationPrincipal User user) {
        Optional<Category> categoryOpt = categoryService.findById(id);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            if (name == null || name.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "اسم الفئة مطلوب");
                return "redirect:/admin/categories/" + id + "/edit";
            }
            
            if (categoryService.existsByNameAndIdNot(name.trim(), id)) {
                redirectAttributes.addFlashAttribute("error", "اسم الفئة موجود بالفعل");
                return "redirect:/admin/categories/" + id + "/edit";
            }
            
            category.setName(name.trim());
            categoryService.save(category);
            redirectAttributes.addFlashAttribute("success", "تم تحديث الفئة بنجاح!");
            return "redirect:/admin/categories";
        }
        return "redirect:/admin/categories";
    }
    
    @DeleteMapping("/{id}")
    public String destroy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoryService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "تم حذف الفئة بنجاح!");
        return "redirect:/admin/categories";
    }
}

