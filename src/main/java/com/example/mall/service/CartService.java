package com.example.mall.service;

import org.springframework.stereotype.Service;

import com.example.mall.dto.CartItemDto;
import com.example.mall.entity.Cart;
import com.example.mall.entity.CartItem;
import com.example.mall.entity.Item;
import com.example.mall.entity.Member;
import com.example.mall.repository.CartItemRepository;
import com.example.mall.repository.CartRepository;
import com.example.mall.repository.ItemRepository;
import com.example.mall.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email){
        Item item = itemRepository.findById(cartItemDto.getItemId())
            .orElseThrow(EntityNotFoundException::new);
        Member member =memberRepository.findByEmail(email);
        
        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem =
        cartItemRepository.findByIdAndItemId(cart.getId(), item.getId());

        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            CartItem cartItem =
            CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
        
    }
    
}
