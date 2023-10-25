package com.example.mall.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.example.mall.constant.ItemSellStatus;
import com.example.mall.dto.CartItemDto;
import com.example.mall.entity.CartItem;
import com.example.mall.entity.Item;
import com.example.mall.entity.Member;
import com.example.mall.repository.CartItemRepository;
import com.example.mall.repository.ItemRepository;
import com.example.mall.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.TransactionScoped;

@SpringBootTest
@TransactionScoped
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartServiceTest {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember(){
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart(){
        Item item = saveItem();
        Member member =saveMember();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());

        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());

        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(EntityNotFoundException::new); // new 붙이는 이유
        
        assertEquals(item.getId(), cartItem.getItem().getId());
        assertEquals(cartItemDto.getCount(), cartItem.getCount());
 
    }

    
}
