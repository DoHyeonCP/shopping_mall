package com.example.mall.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.mall.dto.CartDetailDto;
import com.example.mall.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByIdAndItemId(Long cartId, Long itemId);

    //어노테이션 query, querydsl, 나눠쓰는 이유 상기하기)
    @Query("select new com.example.mall.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
        "from CartItem ci, ItemImg im " +
        "join ci.item i " +
        "where ci.cart.id = :cartId " +
        "and im.item.id = ci.item.id " +
        "and im.repimgYn = 'Y'" +
        "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
    
}
