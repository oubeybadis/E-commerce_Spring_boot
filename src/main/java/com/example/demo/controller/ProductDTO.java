package com.example.demo.controller;

import com.example.demo.entity.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDTO {
    public Long id;
    public String name;
    public String description;
    public BigDecimal price;
    public BigDecimal discountPrice;
    public Long categoryId;
    public String categoryName;
    public List<ProductImageDTO> images;
    public List<ColorDTO> colors;
    public List<SizeDTO> sizes;
    
    public ProductDTO() {}
    
    public static ProductDTO fromProduct(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.id = product.getId();
        dto.name = product.getName();
        dto.description = product.getDescription();
        dto.price = product.getPrice();
        dto.discountPrice = product.getDiscountPrice();
        
        if (product.getCategory() != null) {
            dto.categoryId = product.getCategory().getId();
            dto.categoryName = product.getCategory().getName();
        }
        
        dto.images = product.getImages().stream()
                .map(ProductImageDTO::fromProductImage)
                .collect(Collectors.toList());
        
        dto.colors = product.getColors().stream()
                .map(ColorDTO::fromColor)
                .collect(Collectors.toList());
        
        dto.sizes = product.getSizes().stream()
                .map(SizeDTO::fromSize)
                .collect(Collectors.toList());
        
        return dto;
    }
}

class ProductImageDTO {
    public Long id;
    public String imageUrl;
    public Boolean isMain;
    
    public ProductImageDTO() {}
    
    public static ProductImageDTO fromProductImage(ProductImage image) {
        ProductImageDTO dto = new ProductImageDTO();
        dto.id = image.getId();
        
        // Convert relative path to absolute URL
        String imageUrl = image.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // If it's not already a full URL, prepend the base URL
            if (!imageUrl.startsWith("http")) {
                // Remove leading slashes if any
                imageUrl = imageUrl.replaceFirst("^/+", "");
                // Build the full URL with base path /storage/
                imageUrl = "http://127.0.0.1:8080/storage/" + imageUrl;
            }
        }
        
        dto.imageUrl = imageUrl;
        dto.isMain = image.isMain();
        return dto;
    }
}

class ColorDTO {
    public Long id;
    public String name;
    public String hexCode;
    
    public ColorDTO() {}
    
    public static ColorDTO fromColor(Color color) {
        ColorDTO dto = new ColorDTO();
        dto.id = color.getId();
        dto.name = color.getName();
        dto.hexCode = color.getHexCode();
        return dto;
    }
}

class SizeDTO {
    public Long id;
    public String name;
    
    public SizeDTO() {}
    
    public static SizeDTO fromSize(Size size) {
        SizeDTO dto = new SizeDTO();
        dto.id = size.getId();
        dto.name = size.getName();
        return dto;
    }
}

class CategoryDTO {
    public Long id;
    public String name;
    
    public CategoryDTO() {}
    
    public static CategoryDTO fromCategory(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = category.getId();
        dto.name = category.getName();
        return dto;
    }
}

class OrderDTO {
    public Long id;
    public Long productId;
    public String productName;
    public Long customerId;
    public String customerName;
    public Integer quantity;
    public BigDecimal productPrice;
    public BigDecimal sellingPrice;
    public String comment;
    public String createdAt;
    
    public OrderDTO() {}
    
    public static OrderDTO fromOrder(CustomerOrder order) {
        OrderDTO dto = new OrderDTO();
        dto.id = order.getId();
        
        if (order.getProduct() != null) {
            dto.productId = order.getProduct().getId();
            dto.productName = order.getProduct().getName();
        }
        
        if (order.getCustomer() != null) {
            dto.customerId = order.getCustomer().getId();
            dto.customerName = order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName();
        }
        
        dto.quantity = order.getQuantity();
        dto.productPrice = order.getProductPrice();
        dto.sellingPrice = order.getSellingPrice();
        dto.comment = order.getComment();
        
        if (order.getCreatedAt() != null) {
            dto.createdAt = order.getCreatedAt().toString();
        }
        
        return dto;
    }
}
