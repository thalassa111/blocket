package com.springboot.blocket.controllers;

import com.springboot.blocket.dtos.DeleteUserDto;
import com.springboot.blocket.dtos.LoginRequestDto;
import com.springboot.blocket.dtos.UpdateUserDto;
import com.springboot.blocket.dtos.RegisterUserDto;
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

    //to register a new user, using UserDto
    @PostMapping("/user/register")
    public ResponseEntity<String> addCustomer(@RequestBody RegisterUserDto registerUserDto){
        userService.createCustomer(registerUserDto);
        return ResponseEntity.ok("customer added: " + registerUserDto.getName());
    }

    //will generate a token for a user who provide a correct email and password
    @GetMapping("/user/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(userService.generateTokenForUserByEmailAndPassword(loginRequestDto.email, loginRequestDto.password));
    }

    //get a user based on token
    @GetMapping("/user/get-user")
    public ResponseEntity<User> getUser(@RequestParam String token){
        return ResponseEntity.ok(userService.getCustomerByToken(token));
    }

    //return all users
    @GetMapping("/user/get-all-users")
    public ResponseEntity<List<User>> getAllCustomers(){
        return ResponseEntity.ok(userService.getAllCustomers());
    }

    //used to verify the token, nice to have for testing
    @GetMapping("/user/verify-token")
    public String verifyToken(@RequestParam String token){
        return userService.verifyToken(token);
    }

    @PutMapping("/user/update-user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id,
                                           @RequestBody UpdateUserDto updateUserDto,
                                           @RequestHeader("Authorization") String token) {
        var result = this.userService.updateUser(id, updateUserDto, token);
        return ResponseEntity.ok(result);
    }

    //deletes a user, provide the id of who is to be deleted,
    //and token of the one doing the deleting, need to be an admin.
    @DeleteMapping("/user/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id,
                                             @RequestHeader("Authorization") String token)
    {
        String deletedUser = userService.deleteUser(id, token);
        return ResponseEntity.ok(deletedUser);
    }
}