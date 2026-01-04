package com.example.demo.service;

import com.example.demo.entity.Theme;
import com.example.demo.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ThemeService {
    
    @Autowired
    private ThemeRepository themeRepository;
    
    public Optional<Theme> findById(Long id) {
        return themeRepository.findById(id);
    }
    
    public Theme getDefaultTheme() {
        return themeRepository.findById(1L)
            .orElseGet(() -> {
                Theme theme = new Theme();
                theme.setPrimaryColor("#03aec9");
                return themeRepository.save(theme);
            });
    }
    
    public Theme save(Theme theme) {
        return themeRepository.save(theme);
    }
}

