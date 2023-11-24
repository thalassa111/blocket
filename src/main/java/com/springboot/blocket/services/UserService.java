package com.springboot.blocket.services;

import com.springboot.blocket.dtos.DeleteUserDto;
import com.springboot.blocket.dtos.UserCustomerDto;
import com.springboot.blocket.models.User;
import com.springboot.blocket.repositories.UserRepository;
import com.springboot.blocket.utilities.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createCustomer(UserCustomerDto createDto){
         var customer = new User(   createDto.getName(),
                                    createDto.getEmail(),
                                    createDto.getAddress(),
                                    createDto.getRole(),
                                    createDto.getPassword());

         return this.userRepository.save(customer);
    }

    public String generateTokenForUserByEmailAndPassword(String email, String password){
        try {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    System.out.println("user.getID: " + user.getId());
                    return JwtUtil.createToken(String.valueOf(user.getId()), user.getName());
                }
            } else {
                return "email not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error when getting email";
        }
        return "wrong password";
    }

    public User getCustomerByToken(String token){
        String subject = JwtUtil.getSubjectFromToken(token);
        return userRepository.findById(Integer.parseInt(subject));
    }


    //only used for testing
    public List<User> getAllCustomers(){
        return userRepository.findAll();
    }

    //just for testing token
    public String verifyToken(String token){
        boolean isValid = JwtUtil.verifyToken(token);
        if(isValid){
            String subject = JwtUtil.getSubjectFromToken(token);
            return "Token is valid, Subject: " + subject;
        }
        else {
            return "invalid token";
        }
    }

    public String deleteUser(DeleteUserDto deleteUserDto) {
        //get the subject, which is the id, so we can get the user
        String idFromToken = JwtUtil.getSubjectFromToken(deleteUserDto.getToken());
        //get the user, to check for role
        User user = userRepository.findById(Integer.parseInt(idFromToken));
        //get the soon to be deleted user, so we can return its name
        User deletedUser = userRepository.findById(deleteUserDto.getId());
        //check to see if user with below token has admin right to delete
        if(user.getRole().equals("admin")){
            userRepository.deleteById(deleteUserDto.getId());
            return "User has been deleted: " + deletedUser.getName();
        }
        else{
            return "You dont have the rights to delete";
        }
    }
}