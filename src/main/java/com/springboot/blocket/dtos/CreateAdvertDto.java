package com.springboot.blocket.dtos;

import lombok.Getter;

import java.util.Date;

@Getter
public class CreateAdvertDto {
    private String title;
    private String description;
    private Date date;
    private Integer price;
    private String category;
    private String location;
}
