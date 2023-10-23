package com.example.mall.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mall.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    
}
