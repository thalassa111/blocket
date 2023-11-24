package com.springboot.blocket.controllers;

import com.springboot.blocket.models.Advert;
import com.springboot.blocket.services.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdvertController {

    private final AdvertService advertService;

    @Autowired
    public AdvertController(AdvertService advertService) {
        this.advertService = advertService;
    }

    @PostMapping("/advert/create")
    public ResponseEntity<Advert> createAdvert(@RequestBody Advert advert) {
        Advert createdAdvert = advertService.createAdvert(advert);
        return ResponseEntity.ok(createdAdvert);
    }
}