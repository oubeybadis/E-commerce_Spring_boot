package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "phone1", nullable = false)
    private String phone1;
    
    @Column(name = "phone2")
    private String phone2;
    
    @Column(name = "wilaya_id")
    private Long wilayaId;
    
    @Column(name = "commune_id")
    private Long communeId;
    
    @Column(name = "done")
    private Boolean done = false;
    
    @Column(name = "routeur")
    private String routeur;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerOrder> customerOrders = new ArrayList<>();
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPhone1() {
        return phone1;
    }
    
    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }
    
    public String getPhone2() {
        return phone2;
    }
    
    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }
    
    public Long getWilayaId() {
        return wilayaId;
    }
    
    public void setWilayaId(Long wilayaId) {
        this.wilayaId = wilayaId;
    }
    
    public Long getCommuneId() {
        return communeId;
    }
    
    public void setCommuneId(Long communeId) {
        this.communeId = communeId;
    }
    
    public Boolean getDone() {
        return done;
    }
    
    public void setDone(Boolean done) {
        this.done = done;
    }
    
    public String getRouteur() {
        return routeur;
    }
    
    public void setRouteur(String routeur) {
        this.routeur = routeur;
    }
    
    public List<CustomerOrder> getCustomerOrders() {
        return customerOrders;
    }
    
    public void setCustomerOrders(List<CustomerOrder> customerOrders) {
        this.customerOrders = customerOrders;
    }
}

