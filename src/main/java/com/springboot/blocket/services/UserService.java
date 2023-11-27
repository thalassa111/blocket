package com.springboot.blocket.services;

import com.springboot.blocket.dtos.DeleteUserDto;
import com.springboot.blocket.dtos.UserCustomerDto;
import com.springboot.blocket.models.User;
import com.springboot.blocket.repositories.UserRepository;
import com.springboot.blocket.utilities.JwtUtil;
import com.springboot.blocket.utilities.PasswordEncoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoderUtil passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoderUtil passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createCustomer(UserCustomerDto createDto){
        String salt = BCrypt.gensalt();
        var customer = new User(   createDto.getName(),
                                    createDto.getEmail(),
                                    createDto.getAddress(),
                                    createDto.getRole(),
                                    passwordEncoder.encodePassword(createDto.getPassword(), salt),
                                    salt);
         return this.userRepository.save(customer);
    }

    public String generateTokenForUserByEmailAndPassword(String email, String password){
        try {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                //will compare the raw password with the hashed one along with the salt, if they match, its ok
                if(passwordEncoder.verifyPassword(password, user.password, user.getSalt())){
                    System.out.println("user.getID: " + user.getId());
                    return "Generated token: " + JwtUtil.createToken(String.valueOf(user.getId()), user.getName());
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
            String id = JwtUtil.getSubjectFromToken(token);
            User user = userRepository.findById(Integer.parseInt(id));
            return "Token is valid name: " + user.getName() + "  id: " + user.getId();
        }
        else {
            return "invalid token";
        }
    }

    public String deleteUser(DeleteUserDto deleteUserDto) {
        //check token, if its false, dont continue
        if(!JwtUtil.verifyToken(deleteUserDto.getToken())){
            return "invalid token";
        }
        //get the subject, which is the id, so we can get the user
        String idFromToken = JwtUtil.getSubjectFromToken(deleteUserDto.getToken());
        //get the user, to check for role
        User user = userRepository.findById(Integer.parseInt(idFromToken));
        //get the soon to be deleted user, so we can return its name
        User userToBeDeleted = userRepository.findById(deleteUserDto.getId());
        //check if the user to be deleted exists
        if(userToBeDeleted == null){
            return "user not found";
        }
        //check to see if user with below token has admin right to delete
        if(user.getRole().equals("admin")){
            userRepository.deleteById(deleteUserDto.getId());
            return "User has been deleted: " + userToBeDeleted.getName();
        }
        else{
            return "You don't have the rights to delete";
        }
    }
}