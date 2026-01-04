package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "delevery")
public class DeleveryPrice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "code")
    private String code;
    
    @Column(name = "wilaya")
    private String wilaya;
    
    @Column(name = "tarif")
    private BigDecimal tarif;
    
    @Column(name = "stopdesk")
    private BigDecimal stopdesk;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getWilaya() {
        return wilaya;
    }
    
    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }
    
    public BigDecimal getTarif() {
        return tarif;
    }
    
    public void setTarif(BigDecimal tarif) {
        this.tarif = tarif;
    }
    
    public BigDecimal getStopdesk() {
        return stopdesk;
    }
    
    public void setStopdesk(BigDecimal stopdesk) {
        this.stopdesk = stopdesk;
    }
}

