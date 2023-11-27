package com.springboot.blocket.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateAdvertDto {
    private String title;
    private String description;
    private Date date;
    private Integer price;
    private String category;
    private String location;
}
