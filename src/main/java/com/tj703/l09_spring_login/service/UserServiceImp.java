package com.tj703.l09_spring_login.service;

import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> login(String id, String pw) {
        return userRepository.findByIdAndPw(id, pw);
    }
}
