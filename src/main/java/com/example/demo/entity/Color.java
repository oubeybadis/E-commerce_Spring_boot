package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "colors")
public class Color {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "hex_code")
    private String hexCode;
    
    @ManyToMany(mappedBy = "colors")
    private List<Product> products = new ArrayList<>();
    
    @OneToMany(mappedBy = "color", cascade = CascadeType.ALL)
    private List<CustomerOrder> customerOrders = new ArrayList<>();
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getHexCode() {
        return hexCode;
    }
    
    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }
    
    public List<Product> getProducts() {
        return products;
    }
    
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    public List<CustomerOrder> getCustomerOrders() {
        return customerOrders;
    }
    
    public void setCustomerOrders(List<CustomerOrder> customerOrders) {
        this.customerOrders = customerOrders;
    }
}

