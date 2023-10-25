package com.example.mall.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩을 통해 orderItem만 따로 불러올 수 있도록
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY) //즉시로딩은 너무 무거음 지연로딩을 통해 성능개선
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문가격

    private int count; //수량

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice(){
        return orderPrice*count;
    }
}
