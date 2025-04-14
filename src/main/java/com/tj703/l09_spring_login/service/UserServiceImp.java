package com.tj703.l09_spring_login.service;

import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Override
    public Optional<User> login(String id, String pw) {
        //pw 유저가 입력한 값(평문 1234)
        Optional<User> userOpt=userRepository.findById(id);
        if(userOpt.isPresent()){
            User user=userOpt.get();
            String hashPw=user.getPw(); //저장된 해쉬코드
            //$2a$10$HDA7deEKi9SlqcFSJriojOBQMDCNYDdwd0U87qRXhc5f6aQ5oveoS
            boolean check=BCrypt.checkpw(pw,hashPw);

        }
        // 스프링 security 가 자동으로 구현

        return userRepository.findByIdAndPw(id, pw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void guestSignup(User user) {
        User existUser=entityManager.find(User.class, user.getId());
        if(existUser!=null) throw new IllegalArgumentException("User already exists");
        user.setRole("GUEST");
        String pw=BCrypt.hashpw(user.getPw(), BCrypt.gensalt());
        user.setPw(pw);
        entityManager.persist(user);
    }
}
