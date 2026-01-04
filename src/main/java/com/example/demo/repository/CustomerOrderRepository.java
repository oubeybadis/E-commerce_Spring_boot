package com.example.demo.repository;

import com.example.demo.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByCustomerId(Long customerId);
    List<CustomerOrder> findByProductId(Long productId);
    List<CustomerOrder> findByStatusId(Long statusId);
    
    @Query("SELECT o FROM CustomerOrder o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate")
    List<CustomerOrder> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}

