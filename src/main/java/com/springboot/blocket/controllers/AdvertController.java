package com.springboot.blocket.controllers;

import com.springboot.blocket.models.Advert;
import com.springboot.blocket.services.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @GetMapping("/advert/{id}")
    public ResponseEntity<Advert> getAdvertById(@PathVariable int id) {
        Optional<Advert> advert = advertService.getAdvertById(id);
        return advert.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/advert/by-category/{category}")
    public ResponseEntity<List<Advert>> getAdvertsByCategory(@PathVariable String category) {
        List<Advert> adverts = advertService.getAdvertsByCategory(category);
        return ResponseEntity.ok(adverts);
    }
}