package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ColorService colorService;
    
    @Autowired
    private SizeService sizeService;
    
    @Autowired
    private ProductImageRepository productImageRepository;
    
    @Autowired
    private ThemeService themeService;
    
    @Value("${file.upload-dir:storage}")
    private String uploadDir;
    
    @GetMapping
    public String index(Model model, @AuthenticationPrincipal User user) {
        List<Product> products = productService.findAllWithRelations();
        model.addAttribute("products", products);
        model.addAttribute("user", user);
        model.addAttribute("themeSetting", themeService.getDefaultTheme());
        model.addAttribute("title", "المنتجات");
        return "admin/products/index";
    }
    
    @GetMapping("/create")
    public String create(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        model.addAttribute("user", user);
        model.addAttribute("themeSetting", themeService.getDefaultTheme());
        model.addAttribute("title", "إضافة منتج");
        return "admin/products/create";
    }
    
    @PostMapping
    public String store(@RequestParam("name") String name,
                       @RequestParam("description") String description,
                       @RequestParam("category_id") Long categoryId,
                       @RequestParam("price") BigDecimal price,
                       @RequestParam(value = "discount_price", required = false) BigDecimal discountPrice,
                       @RequestParam(value = "main_image", required = false) MultipartFile mainImage,
                       @RequestParam(value = "images", required = false) MultipartFile[] images,
                       @RequestParam(value = "colors", required = false) List<Long> colorIds,
                       @RequestParam(value = "sizes", required = false) List<Long> sizeIds,
                       RedirectAttributes redirectAttributes,
                       @AuthenticationPrincipal User user) {
        
        try {
            Optional<Category> categoryOpt = categoryService.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "الفئة غير موجودة");
                return "redirect:/admin/products/create";
            }
            
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setCategory(categoryOpt.get());
            product.setPrice(price);
            product.setDiscountPrice(discountPrice);
            
            product = productService.save(product);
            
            // Handle main image
            if (mainImage != null && !mainImage.isEmpty() && mainImage.getSize() > 0) {
                try {
                    String imagePath = saveImage(mainImage);
                    ProductImage productImage = new ProductImage();
                    productImage.setProduct(product);
                    productImage.setImageUrl(imagePath);
                    productImage.setMain(true);
                    productImageRepository.save(productImage);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save main image: " + e.getMessage(), e);
                }
            }
            
            // Handle additional images
            if (images != null && images.length > 0) {
                for (MultipartFile image : images) {
                    if (image != null && !image.isEmpty() && image.getSize() > 0) {
                        try {
                            String imagePath = saveImage(image);
                            ProductImage productImage = new ProductImage();
                            productImage.setProduct(product);
                            productImage.setImageUrl(imagePath);
                            productImage.setMain(false);
                            productImageRepository.save(productImage);
                        } catch (IOException e) {
                            // Log error but continue with other images
                            System.err.println("Failed to save additional image: " + e.getMessage());
                        }
                    }
                }
            }
            
            // Handle colors
            if (colorIds != null && !colorIds.isEmpty()) {
                List<Color> colors = new ArrayList<>();
                for (Long colorId : colorIds) {
                    colorService.findById(colorId).ifPresent(colors::add);
                }
                product.setColors(colors);
                productService.save(product);
            }
            
            // Handle sizes
            if (sizeIds != null && !sizeIds.isEmpty()) {
                List<Size> sizes = new ArrayList<>();
                for (Long sizeId : sizeIds) {
                    sizeService.findById(sizeId).ifPresent(sizes::add);
                }
                product.setSizes(sizes);
                productService.save(product);
            }
            
            redirectAttributes.addFlashAttribute("success", "تم إنشاء المنتج بنجاح!");
            return "redirect:/admin/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "حدث خطأ: " + e.getMessage());
            return "redirect:/admin/products/create";
        }
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        Optional<Product> productOpt = productService.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("colors", colorService.findAll());
            model.addAttribute("sizes", sizeService.findAll());
            model.addAttribute("user", user);
            model.addAttribute("themeSetting", themeService.getDefaultTheme());
            model.addAttribute("title", "تعديل المنتج");
            return "admin/products/edit";
        }
        return "redirect:/admin/products";
    }
    
    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                        @RequestParam("name") String name,
                        @RequestParam("description") String description,
                        @RequestParam("category_id") Long categoryId,
                        @RequestParam("price") BigDecimal price,
                        @RequestParam(value = "discount_price", required = false) BigDecimal discountPrice,
                        @RequestParam(value = "main_image", required = false) MultipartFile mainImage,
                        @RequestParam(value = "images", required = false) MultipartFile[] images,
                        @RequestParam(value = "colors", required = false) List<Long> colorIds,
                        @RequestParam(value = "sizes", required = false) List<Long> sizeIds,
                        RedirectAttributes redirectAttributes,
                        @AuthenticationPrincipal User user) {
        
        Optional<Product> productOpt = productService.findById(id);
        if (productOpt.isEmpty()) {
            return "redirect:/admin/products";
        }
        
        try {
            Product product = productOpt.get();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setDiscountPrice(discountPrice);
            
            Optional<Category> categoryOpt = categoryService.findById(categoryId);
            if (categoryOpt.isPresent()) {
                product.setCategory(categoryOpt.get());
            }
            
            // Handle main image
            if (mainImage != null && !mainImage.isEmpty() && mainImage.getSize() > 0) {
                // Delete old main image
                product.getImages().stream()
                    .filter(ProductImage::isMain)
                    .findFirst()
                    .ifPresent(img -> {
                        deleteImageFile(img.getImageUrl());
                        productImageRepository.delete(img);
                    });
                
                try {
                    String imagePath = saveImage(mainImage);
                    ProductImage productImage = new ProductImage();
                    productImage.setProduct(product);
                    productImage.setImageUrl(imagePath);
                    productImage.setMain(true);
                    productImageRepository.save(productImage);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save main image: " + e.getMessage(), e);
                }
            }
            
            // Handle additional images
            // Only add new images, don't delete existing ones
            if (images != null && images.length > 0) {
                for (MultipartFile image : images) {
                    if (image != null && !image.isEmpty() && image.getSize() > 0) {
                        try {
                            String imagePath = saveImage(image);
                            ProductImage productImage = new ProductImage();
                            productImage.setProduct(product);
                            productImage.setImageUrl(imagePath);
                            productImage.setMain(false);
                            productImageRepository.save(productImage);
                        } catch (IOException e) {
                            // Log error but continue with other images
                            System.err.println("Failed to save additional image: " + e.getMessage());
                        }
                    }
                }
            }
            
            // Handle colors
            if (colorIds != null) {
                List<Color> colors = new ArrayList<>();
                for (Long colorId : colorIds) {
                    colorService.findById(colorId).ifPresent(colors::add);
                }
                product.setColors(colors);
            }
            
            // Handle sizes
            if (sizeIds != null) {
                List<Size> sizes = new ArrayList<>();
                for (Long sizeId : sizeIds) {
                    sizeService.findById(sizeId).ifPresent(sizes::add);
                }
                product.setSizes(sizes);
            }
            
            productService.save(product);
            redirectAttributes.addFlashAttribute("success", "تم تحديث المنتج بنجاح!");
            return "redirect:/admin/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "حدث خطأ: " + e.getMessage());
            return "redirect:/admin/products/" + id + "/edit";
        }
    }
    
    @DeleteMapping("/{id}")
    public String destroy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Product> productOpt = productService.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            // Delete images
            product.getImages().forEach(img -> {
                deleteImageFile(img.getImageUrl());
                productImageRepository.delete(img);
            });
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "تم حذف المنتج بنجاح!");
        }
        return "redirect:/admin/products";
    }
    
    @DeleteMapping("/{productId}/images/{imageId}")
    @ResponseBody
    public String deleteImage(@PathVariable Long productId, @PathVariable Long imageId) {
        Optional<ProductImage> imageOpt = productImageRepository.findById(imageId);
        if (imageOpt.isPresent()) {
            ProductImage image = imageOpt.get();
            if (image.getProduct().getId().equals(productId)) {
                deleteImageFile(image.getImageUrl());
                productImageRepository.delete(image);
                return "{\"success\": true}";
            }
        }
        return "{\"error\": \"Image not found\"}";
    }
    
    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir + "/product_images");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        return "product_images/" + fileName;
    }
    
    private void deleteImageFile(String imageUrl) {
        try {
            Path filePath = Paths.get(uploadDir, imageUrl);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log error but don't fail
        }
    }
}

