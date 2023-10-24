package com.example.mall.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mall.entity.ItemImg;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}