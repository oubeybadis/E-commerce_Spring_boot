package com.example.demo.repository;

import com.example.demo.entity.DeleveryPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeleveryPriceRepository extends JpaRepository<DeleveryPrice, Long> {
    List<DeleveryPrice> findByWilaya(String wilaya);
    Optional<DeleveryPrice> findByCode(String code);
}

