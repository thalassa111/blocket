package com.springboot.blocket.services;

import com.springboot.blocket.dtos.UpdateAdvertDto;
import com.springboot.blocket.models.Advert;
import com.springboot.blocket.repositories.AdvertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertService {

    private AdvertRepository advertRepository;

    @Autowired
    public AdvertService(AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }
    public Advert createAdvert(Advert advertDto){
        var advert = new Advert(   advertDto.getTitle(),
                advertDto.getDescription(),
                advertDto.getDate(),
                advertDto.getPrice(),
                advertDto.getCategory(),
                advertDto.getLocation());

        return this.advertRepository.save(advert);
    }
    public List<Advert> getAllAdverts() {
        return advertRepository.findAll();
    }

}
