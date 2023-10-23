package com.example.mall.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.mall.dto.ItemFormDto;
import com.example.mall.entity.Item;
import com.example.mall.entity.ItemImg;
import com.example.mall.repository.ItemImgRepository;
import com.example.mall.repository.ItemRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    @NonNull private ItemImgService itemImgService;
    @NonNull private ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);
        
        // 이미지 등록
        for(int i = 0; i < itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0)
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("n");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }


        return item.getId();
    }
    
}
