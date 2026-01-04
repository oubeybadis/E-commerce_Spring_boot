package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "themes")
public class Theme {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "logo")
    private String logo;
    
    @Column(name = "primary_color")
    private String primaryColor;
    
    @Column(name = "landing")
    private String landing;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getLogo() {
        return logo;
    }
    
    public void setLogo(String logo) {
        this.logo = logo;
    }
    
    public String getPrimaryColor() {
        return primaryColor;
    }
    
    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }
    
    public String getLanding() {
        return landing;
    }
    
    public void setLanding(String landing) {
        this.landing = landing;
    }
}

