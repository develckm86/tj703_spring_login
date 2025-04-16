package com.tj703.l09_spring_login.service;

import com.tj703.l09_spring_login.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    //Optional<User> login(String id, String pw);
    List<User> findAll();
    User findById(String id);
    void save(User user);
}
