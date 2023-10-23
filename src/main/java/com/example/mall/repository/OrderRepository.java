package com.example.mall.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mall.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
    
}
