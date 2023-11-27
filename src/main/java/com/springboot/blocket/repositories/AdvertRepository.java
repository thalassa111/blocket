package com.springboot.blocket.repositories;

import com.springboot.blocket.models.Advert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertRepository extends JpaRepository<Advert, Integer> {
List<Advert> findAll();
}