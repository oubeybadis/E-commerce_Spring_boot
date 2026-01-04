package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    
    public List<Product> findAllWithRelations() {
        // Use findAll and let Hibernate load relationships lazily
        // This avoids MultipleBagFetchException
        List<Product> products = productRepository.findAll();
        // Initialize images to avoid lazy loading issues
        products.forEach(p -> {
            if (p.getImages() != null) {
                p.getImages().size(); // Force initialization
            }
        });
        return products;
    }
    
    public Optional<Product> findById(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        // Initialize relationships to avoid lazy loading issues
        productOpt.ifPresent(product -> {
            if (product.getImages() != null) {
                product.getImages().size(); // Force initialization
            }
            if (product.getColors() != null) {
                product.getColors().size(); // Force initialization
            }
            if (product.getSizes() != null) {
                product.getSizes().size(); // Force initialization
            }
        });
        return productOpt;
    }
    
    public Product save(Product product) {
        return productRepository.save(product);
    }
    
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
    
    public List<Product> findByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}

