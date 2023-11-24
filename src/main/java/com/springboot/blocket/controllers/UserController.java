package com.springboot.blocket.controllers;

import com.springboot.blocket.dtos.DeleteUserDto;
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

    //will generate a token for a user who provide a correct email and password
    @GetMapping("/user/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok("Generated token: " + userService.generateTokenForUserByEmailAndPassword(loginRequestDto.email, loginRequestDto.password));
    }

    //get a user based on token
    @GetMapping("/user/get-user")
    public ResponseEntity<User> getUser(@RequestParam String token){
        return ResponseEntity.ok(userService.getCustomerByToken(token));
    }

    @GetMapping("/user/get-all-users")
    public ResponseEntity<List<User>> getAllCustomers(){
        return ResponseEntity.ok(userService.getAllCustomers());
    }

    @GetMapping("/user/verify-token")
    public String verifyToken(@RequestParam String token){
        return userService.verifyToken(token);
    }

    //deletes a user, provide the id of who is to be deleted,
    //and token of the one doing the deleting, need to be an admin.
    @DeleteMapping("/user/delete-user")
    public ResponseEntity<String> deleteUser(@RequestBody DeleteUserDto deleteUserDto)
    {
        String deletedUser = userService.deleteUser(deleteUserDto);
        return ResponseEntity.ok(deletedUser);
    }
}