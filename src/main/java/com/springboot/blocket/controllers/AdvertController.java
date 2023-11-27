package com.springboot.blocket.controllers;

import com.springboot.blocket.dtos.UpdateAdvertDto;
import com.springboot.blocket.models.Advert;
import com.springboot.blocket.services.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/advert/all")
    public ResponseEntity<List<Advert>> getAllAdverts() {
        List<Advert> adverts = advertService.getAllAdverts();
        return ResponseEntity.ok(adverts);
    }

    @PutMapping("/advert/update/{id}")
    public ResponseEntity<Advert> updateAdvert(@PathVariable("id") int id, @RequestBody UpdateAdvertDto updatedAdvertDto) {
        Advert updatedAdvert = advertService.updateAdvert(id, updatedAdvertDto);
        return ResponseEntity.ok(updatedAdvert);
    }
}