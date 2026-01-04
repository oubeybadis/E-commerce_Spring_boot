package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded files from storage directory
        Path uploadPath = Paths.get("storage").toAbsolutePath();
        // Use toUri() which properly formats the path for both Windows and Unix
        // This returns file:///D:/path/ on Windows and file:///path/ on Unix
        String fileUrl = uploadPath.toUri().toString();
        // Ensure it ends with /
        if (!fileUrl.endsWith("/")) {
            fileUrl += "/";
        }
        registry.addResourceHandler("/storage/**")
            .addResourceLocations(fileUrl)
            .setCachePeriod(0); // Disable caching for development
    }
    
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}

