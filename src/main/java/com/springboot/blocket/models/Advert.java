package com.springboot.blocket.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Advert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public Advert(String title, String description, Date date, Integer price, String category, String location) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.price = price;
        this.category = category;
        this.location = location;
    }

    @Column(nullable = false)
    public String title;

    @Column(nullable = false)
    public String description;

    @Column(nullable = false)
    public Date date;


    @Column(nullable = false)
    public int price;

    @Column(nullable = false)
    public String category;

    @Column(nullable = false)
    public String location;

}