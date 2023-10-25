package com.example.mall.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.mall.dto.ItemSearchDto;
import com.example.mall.dto.MainItemDto;
import com.example.mall.entity.Item;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
