package com.example.mall.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.example.mall.constant.ItemSellStatus;
import com.example.mall.dto.ItemSearchDto;
import com.example.mall.dto.MainItemDto;
import com.example.mall.dto.QMainItemDto;
import com.example.mall.entity.Item;
import com.example.mall.entity.QItem;
import com.example.mall.entity.QItemImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus ==
        null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
         }else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        
        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createBy", searchBy)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }
    
    @Override // 책이랑 버전 다름
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        JPAQuery<Item> results = queryFactory
            .selectFrom(QItem.item)
            .where(
                regDtsAfter(itemSearchDto.getSearchDateType()),
                searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                searchByLike(itemSearchDto.getSearchBy(),
                itemSearchDto.getSearchQuery()))
            .orderBy(QItem.item.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());


        List<Item> content = results.fetch();
                
        JPAQuery<Long> countQuery = queryFactory
            .select(QItem.item.id.count())
            .from(QItem.item)
            .where(
                regDtsAfter(itemSearchDto.getSearchDateType()),
                searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())
            );

        long total = countQuery.fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ?
        null : QItem.item.itemNm.like("%" + searchQuery + "$");
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        JPAQuery<MainItemDto> results = queryFactory
            .select(
                new QMainItemDto(
                    item.id,
                    item.itemNm,
                    item.itemDetail,
                    itemImg.imgUrl,
                    item.price)
            )
            .from(itemImg)
            .join(itemImg.item, item)
            .where(itemImg.repimgYn.eq("Y"))
            .where(itemNmLike(itemSearchDto.getSearchQuery()))
            .orderBy(item.id.desc());
            

        List<MainItemDto> content = results
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        
        long total = queryFactory
            .select(item.id.count())
            .from(itemImg)
            .join(itemImg.item, item)
            .where(itemImg.repimgYn.eq("Y"))
            .where(itemNmLike(itemSearchDto.getSearchQuery())) // Assuming this is a valid method
            .fetchOne();


        return new PageImpl<>(content, pageable, total);
    }

    
}
