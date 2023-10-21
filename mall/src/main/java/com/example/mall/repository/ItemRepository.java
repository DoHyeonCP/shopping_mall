package com.example.mall.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mall.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{

}

