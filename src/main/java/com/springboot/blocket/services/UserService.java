package com.springboot.blocket.services;

import com.springboot.blocket.dtos.DeleteUserDto;
import com.springboot.blocket.dtos.RegisterUserDto;
import com.springboot.blocket.dtos.UpdateUserDto;
import com.springboot.blocket.models.User;
import com.springboot.blocket.repositories.UserRepository;
import com.springboot.blocket.utilities.JwtUtil;
import com.springboot.blocket.utilities.PasswordEncoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoderUtil passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoderUtil passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createCustomer(RegisterUserDto createDto){
        String salt = BCrypt.gensalt();
        var customer = new User(   createDto.getName(),
                                    createDto.getEmail(),
                                    createDto.getAddress(),
                                    passwordEncoder.encodePassword(createDto.getPassword(), salt),
                                    salt,
                                    createDto.getAuthority());

         return this.userRepository.save(customer);
    }

    public String generateTokenForUserByEmailAndPassword(String email, String password) {
        try {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                //will compare the raw password with the hashed one along with the salt, if they match, its ok
                if(passwordEncoder.verifyPassword(password, user.password, user.getSalt())){
                    //return a token with id, authority and name in the payload
                    return "Generated token: " + JwtUtil.createToken(String.valueOf(user.getId()), user.getAuthority(), user.getName());
                }
            } else {
                return "email/user not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error when getting email";
        }
        return "wrong password";
    }

    public User getCustomerByToken(String token) {
        String subject = JwtUtil.getSubjectFromToken(token);
        return userRepository.findById(Integer.parseInt(subject));
    }

    //only used for testing
    public List<User> getAllCustomers(){
        return userRepository.findAll();
    }

    // just for testing token
    public String verifyToken(String token) {
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

    public User updateUser (int sid, UpdateUserDto dto, String token) {
        //check token validity
        if(JwtUtil.verifyToken(token)) {
            //need to compare the id from token and the one being updated
            int tokenId = Integer.parseInt(JwtUtil.getSubjectFromToken(token));
            var user = this.userRepository.findById(sid);
            if(user.getId() == tokenId) {
                if (dto.getName().isPresent()) {
                    user.setName(dto.getName().get());
                }
                if (dto.getEmail().isPresent()) {
                    user.setEmail(dto.getEmail().get());
                }
                if (dto.getAddress().isPresent()) {
                    user.setAddress(dto.getAddress().get());
                }
                return this.userRepository.save(user);
            } else{
                System.out.println("you dont have the permission");
                return null;
            }
        }else{
            System.out.println("invalid token");
            return null;
        }
    }

    public String deleteUser(int id, String token) {
        //check token, if its false, dont continue
        if(!JwtUtil.verifyToken(token)){
            return "invalid token";
        }
        //get the subject, which is the id, so we can get the user
        String idFromToken = JwtUtil.getSubjectFromToken(token);
        //get the user, to check for role
        User user = userRepository.findById(Integer.parseInt(idFromToken));
        //get the soon to be deleted user, so we can return its name
        User userToBeDeleted = userRepository.findById(id);
        //check if the user to be deleted exists
        if(userToBeDeleted == null){
            return "user not found";
        }
        //check to see if user with below token has admin right to delete
        if(user.getAuthorities().toString().contains("ADMIN")){
            userRepository.deleteById(id);
            return "User has been deleted: " + userToBeDeleted.getName();
        }
        else{
            return "You don't have the rights to delete";
        }
    }

    //has to do with spring security, basically authentication and authorization
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = this.userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user '" + username + "'."));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getAuthority())
                .build();
    }

    public User getUserByToken(String token) {
        //gets id from token
        String subject = JwtUtil.getSubjectFromToken(token);
        return userRepository.findById(Integer.parseInt(subject));
    }
}