package com.springboot.blocket.controllers;

import com.springboot.blocket.dtos.LoginRequestDto;
import com.springboot.blocket.dtos.UserCustomerDto;
import com.springboot.blocket.models.User;
import com.springboot.blocket.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<String> addCustomer(@RequestBody UserCustomerDto customerDto){
        userService.createCustomer(customerDto);
        return ResponseEntity.ok("customer added: " + customerDto.getName());
    }

    @GetMapping("/user/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok("Generated token: " + userService.getUserByEmail(loginRequestDto.email, loginRequestDto.password));
    }

    @GetMapping("/user/get-all-users")
    public ResponseEntity<List<User>> getAllCustomers(){
        return ResponseEntity.ok(userService.getAllCustomers());
    }

    @GetMapping("/user/verify-token")
    public String verifyToken(@RequestParam String token){
        return userService.verifyToken(token);
    }
}
