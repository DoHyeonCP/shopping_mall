package com.example.mall.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mall.dto.OrderDto;
import com.example.mall.dto.OrderHistDto;
import com.example.mall.dto.OrderItemDto;
import com.example.mall.entity.Item;
import com.example.mall.entity.ItemImg;
import com.example.mall.entity.Member;
import com.example.mall.entity.Order;
import com.example.mall.entity.OrderItem;
import com.example.mall.repository.ItemImgRepository;
import com.example.mall.repository.ItemRepository;
import com.example.mall.repository.MemberRepository;
import com.example.mall.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email){
        Item item = itemRepository.findById(orderDto.getItemId())
            .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member,orderItemList);
        orderRepository.save(order);
        

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for(Order order : orders){
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem orderItem :orderItems){
                ItemImg itemImg = itemImgRepository
                .findByItemIdAndRepimgYn(orderItem.getItem().getId(),"Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

        orderHistDtos.add(orderHistDto);

        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }
    
}
