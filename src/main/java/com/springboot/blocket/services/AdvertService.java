package com.springboot.blocket.services;

import com.springboot.blocket.dtos.UpdateAdvertDto;
import com.springboot.blocket.models.Advert;
import com.springboot.blocket.repositories.AdvertRepository;

import com.springboot.blocket.utilities.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdvertService {

    private AdvertRepository advertRepository;
    private UserService userService;

    @Autowired
    public AdvertService(AdvertRepository advertRepository, UserService userService) {
        this.advertRepository = advertRepository;
        this.userService = userService;
    }
    public Advert createAdvert(Advert advertDto, String token){
        var advert = new Advert(    advertDto.getTitle(),
                                    advertDto.getDescription(),
                                    advertDto.getDate(),
                                    advertDto.getPrice(),
                                    advertDto.getCategory(),
                                    advertDto.getLocation(),
                                    userService.getUserByToken(token));

        return this.advertRepository.save(advert);
    }
    public List<Advert> getAllAdverts() {
        return advertRepository.findAll();
    }
    public Optional<Advert> getAdvertById(int id) {
        return advertRepository.findById(id);
    }
    public List<Advert> getAdvertsByCategory(String category) {
        return advertRepository.findByCategory(category);
    }

    public Advert updateAdvert(int id, UpdateAdvertDto updatedAdvertDto, String token) {
        //check to see if token is valid
        if(JwtUtil.verifyToken(token)) {
            int tokenId = Integer.parseInt(JwtUtil.getSubjectFromToken(token));
            //get the advert
            Optional<Advert> optionalAdvert = advertRepository.findById(id);
            Advert tmpAdvert = optionalAdvert.orElse(null);
            //we need to id of the user who created the advert
            int idUserInAdvert = tmpAdvert.getUser().getId();
            //compare if the user who created the advert, matches the user of the token
            if (idUserInAdvert == tokenId) {
                Advert advert = advertRepository.findById(id).orElseThrow();
                advert.setTitle(updatedAdvertDto.getTitle());
                advert.setDescription(updatedAdvertDto.getDescription());
                advert.setDate(updatedAdvertDto.getDate());
                advert.setPrice(updatedAdvertDto.getPrice());
                advert.setCategory(updatedAdvertDto.getCategory());
                advert.setLocation(updatedAdvertDto.getLocation());
                return advertRepository.save(advert);
            }else{
                System.out.println("not authorized");
                return null;
            }
        }else {
            System.out.println("token invalid");
            return null;
        }
    }

    public List<Advert> getAllUserAdverts(String token) {
        //verify if token is legit
        if (JwtUtil.verifyToken(token)) {
            //extract id from token
            String tokenId = JwtUtil.getSubjectFromToken(token);
            //get the list of adverts matching the extracted id
            List<Advert> userAdverts = advertRepository.findByUserId(Integer.parseInt(tokenId));
            return userAdverts;
        } else {
            System.out.println("token invalid");
            return null;
        }
    }
    public void deleteAdvert(int id, String token) {
        // Validate the token and get user ID
        if (JwtUtil.verifyToken(token)) {
            int userIdFromToken = Integer.parseInt(JwtUtil.getSubjectFromToken(token));

            // Check if the user who created the advert matches the user from the token
            Optional<Advert> optionalAdvert = advertRepository.findById(id);
            Advert advert = optionalAdvert.orElse(null);

            if (advert != null && advert.getUser().getId() == userIdFromToken) {
                advertRepository.deleteById(id);
            } else {
                throw new RuntimeException("Not authorized to delete this advert");
            }
        } else {
            throw new RuntimeException("Invalid token");
        }
    }
}
