package com.springboot.blocket.dtos;

import lombok.Getter;

@Getter
public class RegisterUserDto {
    private String name;
    private String email;
    private String address;
    private String password;
    private String authority;
}
