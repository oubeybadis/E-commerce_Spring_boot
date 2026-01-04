package com.example.demo.service;

import com.example.demo.entity.CustomerOrder;
import com.example.demo.repository.CustomerOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerOrderService {
    
    @Autowired
    private CustomerOrderRepository customerOrderRepository;
    
    public List<CustomerOrder> findAll() {
        return customerOrderRepository.findAll();
    }
    
    public Optional<CustomerOrder> findById(Long id) {
        return customerOrderRepository.findById(id);
    }
    
    public CustomerOrder save(CustomerOrder order) {
        return customerOrderRepository.save(order);
    }
    
    public void deleteById(Long id) {
        customerOrderRepository.deleteById(id);
    }
    
    public List<CustomerOrder> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return customerOrderRepository.findByDateRange(startDate, endDate);
    }
    
    public List<CustomerOrder> findByStatusId(Long statusId) {
        return customerOrderRepository.findByStatusId(statusId);
    }
}

