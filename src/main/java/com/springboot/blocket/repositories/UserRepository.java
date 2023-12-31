package com.springboot.blocket.repositories;

import com.springboot.blocket.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll();

    User findByEmail(String email);

    User findById(int id);

    void deleteById(int id);

    Optional<User> findByName(String name);
}
