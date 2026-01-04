package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    
    public List<Category> findAllOrderByCreatedAtDesc() {
        return categoryRepository.findAll().stream()
            .sorted((a, b) -> {
                Long idA = a.getId() != null ? a.getId() : 0L;
                Long idB = b.getId() != null ? b.getId() : 0L;
                return idB.compareTo(idA);
            })
            .toList();
    }
    
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
    
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
    
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
    
    public boolean existsByName(String name) {
        return categoryRepository.findAll().stream()
            .anyMatch(c -> c.getName() != null && c.getName().equals(name));
    }
    
    public boolean existsByNameAndIdNot(String name, Long id) {
        return categoryRepository.findAll().stream()
            .anyMatch(c -> c.getName() != null && c.getName().equals(name) 
                && c.getId() != null && !c.getId().equals(id));
    }
}

