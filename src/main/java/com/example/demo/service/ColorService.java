package com.example.demo.service;

import com.example.demo.entity.Color;
import com.example.demo.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ColorService {
    
    @Autowired
    private ColorRepository colorRepository;
    
    public List<Color> findAll() {
        return colorRepository.findAll();
    }
    
    public List<Color> findAllOrderByCreatedAtDesc() {
        return colorRepository.findAll().stream()
            .sorted((a, b) -> {
                Long idA = a.getId() != null ? a.getId() : 0L;
                Long idB = b.getId() != null ? b.getId() : 0L;
                return idB.compareTo(idA);
            })
            .toList();
    }
    
    public Optional<Color> findById(Long id) {
        return colorRepository.findById(id);
    }
    
    public Color save(Color color) {
        return colorRepository.save(color);
    }
    
    public void deleteById(Long id) {
        colorRepository.deleteById(id);
    }
    
    public boolean existsByName(String name) {
        return colorRepository.findAll().stream()
            .anyMatch(c -> c.getName() != null && c.getName().equals(name));
    }
    
    public boolean existsByNameAndIdNot(String name, Long id) {
        return colorRepository.findAll().stream()
            .anyMatch(c -> c.getName() != null && c.getName().equals(name) 
                && c.getId() != null && !c.getId().equals(id));
    }
}

