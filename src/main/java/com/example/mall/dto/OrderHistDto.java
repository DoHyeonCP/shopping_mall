package com.example.mall.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.mall.constant.OrderStatus;
import com.example.mall.entity.Order;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderHistDto {

    public OrderHistDto(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    private Long orderId; //주문 아이디
    
    private String orderDate; // 주문날짜

    private OrderStatus orderStatus; //주문 상태

    //주문 상품 리스트
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }
    
}
