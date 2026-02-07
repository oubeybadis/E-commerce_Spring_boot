package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static files from the external storage directory
        String storagePath = Paths.get("storage").toAbsolutePath().toUri().toString();
        
        registry.addResourceHandler("/storage/**")
                .addResourceLocations(storagePath)
                .setCachePeriod(3600);
        
        // Keep the default static resource handler
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
    }
}
