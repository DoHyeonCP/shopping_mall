package com.example.mall.dto;

import com.example.mall.entity.OrderItem;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice =orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    } // getter와 setter를 예를 들어 아래 private을 선언하면 참조가 되는 이유 설명 블로그

    private String itemNm; //상품명
    
    private int count; //주문수량

    private int orderPrice; //주문 금액

    private String imgUrl; //상품 이미지 경로
}
