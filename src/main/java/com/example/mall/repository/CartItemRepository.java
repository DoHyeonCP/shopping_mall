package com.example.mall.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mall.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByIdAndItemId(Long cartId, Long itemId);
    
}
