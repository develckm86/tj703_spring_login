package com.tj703.l09_spring_login.repository;

import com.tj703.l09_spring_login.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findById() {
        Optional<User> userOpt=userRepository.findById("user1");
        if(userOpt.isPresent()){
            User user=userOpt.get();
            System.out.println(user);
        }
    }


    @Test
    void findByIdAndPw() {
        Optional<User> loginUserOpt=userRepository.findByIdAndPw("user1", "1234");
        if(loginUserOpt.isPresent()){
            User user=loginUserOpt.get();
            System.out.println(user);
        }
    }
}