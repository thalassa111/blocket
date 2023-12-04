package com.springboot.blocket.controllers;

import com.springboot.blocket.dtos.UpdateAdvertDto;
import com.springboot.blocket.models.Advert;
import com.springboot.blocket.services.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Advert> createAdvert(@RequestBody Advert advert,
                                               @RequestHeader("Authorization") String token) {
        Advert createdAdvert = advertService.createAdvert(advert, token);
        return ResponseEntity.ok(createdAdvert);
    }
    //Return a list of all  adverts, yours and others
    @GetMapping("/advert/all")
    public ResponseEntity<List<Advert>> getAllAdverts() {
        List<Advert> adverts = advertService.getAllAdverts();
        return ResponseEntity.ok(adverts);
    }

    @PutMapping("/advert/user/update/{id}")
    public ResponseEntity<Advert> updateAdvert( @PathVariable("id") int id,
                                                @RequestBody UpdateAdvertDto updatedAdvertDto,
                                                @RequestHeader("Authorization") String token) {
        Advert updatedAdvert = advertService.updateAdvert(id, updatedAdvertDto, token);
        return ResponseEntity.ok(updatedAdvert);
    }
    //Return a specfic advert to get information about it
    @GetMapping("/advert/{id}")
    public ResponseEntity<Advert> getAdvertById(@PathVariable int id) {
        Optional<Advert> advert = advertService.getAdvertById(id);
        return advert.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    //Return a list of adverts with a specific category
    @GetMapping("/advert/by-category/{category}")
    public ResponseEntity<List<Advert>> getAdvertsByCategory(@PathVariable String category) {
        List<Advert> adverts = advertService.getAdvertsByCategory(category);
        return ResponseEntity.ok(adverts);
    }

    //Return a list of all adverts based on userId taken from token
    @GetMapping("/advert/user/show-all")
    public ResponseEntity<List<Advert>> getAllUserAdverts(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(advertService.getAllUserAdverts(token));
    }
    //Delete an advert you created, authorized with token
    @DeleteMapping("/advert/delete/{id}")
    public ResponseEntity<String> deleteAdvert(@PathVariable int id,
                                               @RequestHeader("Authorization") String token) {
        try {
            advertService.deleteAdvert(id, token);
            return ResponseEntity.ok("Advert deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}