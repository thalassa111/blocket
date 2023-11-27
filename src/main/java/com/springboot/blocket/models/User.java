package com.springboot.blocket.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public User(String name, String email, String address, String role, String password, String salt){
        this.name = name;
        this.email = email;
        this.address = address;
        this.role = role;
        this.password = password;
        this.salt = salt;

    }

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String email;

    @Column
    public String address;

    @Column(nullable = false)
    public String role;

    @Column(nullable = false)
    public String password;

    @Column(nullable = false)
    public String salt;

}
