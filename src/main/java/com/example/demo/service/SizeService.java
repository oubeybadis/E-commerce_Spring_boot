package com.example.demo.service;

import com.example.demo.entity.Size;
import com.example.demo.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SizeService {
    
    @Autowired
    private SizeRepository sizeRepository;
    
    public List<Size> findAll() {
        return sizeRepository.findAll();
    }
    
    public List<Size> findAllOrderByCreatedAtDesc() {
        return sizeRepository.findAll().stream()
            .sorted((a, b) -> {
                Long idA = a.getId() != null ? a.getId() : 0L;
                Long idB = b.getId() != null ? b.getId() : 0L;
                return idB.compareTo(idA);
            })
            .toList();
    }
    
    public Optional<Size> findById(Long id) {
        return sizeRepository.findById(id);
    }
    
    public Size save(Size size) {
        return sizeRepository.save(size);
    }
    
    public void deleteById(Long id) {
        sizeRepository.deleteById(id);
    }
    
    public boolean existsByName(String name) {
        return sizeRepository.findAll().stream()
            .anyMatch(s -> s.getName() != null && s.getName().equals(name));
    }
    
    public boolean existsByNameAndIdNot(String name, Long id) {
        return sizeRepository.findAll().stream()
            .anyMatch(s -> s.getName() != null && s.getName().equals(name) 
                && s.getId() != null && !s.getId().equals(id));
    }
}

