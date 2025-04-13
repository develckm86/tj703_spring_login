package com.tj703.l09_spring_login.service;

import com.tj703.l09_spring_login.entity.User;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceImpTest {
    @Autowired
    private UserService userService;
    @Test
    void bcrypt() {
        System.out.println(BCrypt.hashpw("1234", BCrypt.gensalt()));
    }
    @Test
    void makeSecretKey(){
        //랜덤하게 비밀번호 생성
        SecretKey key1 = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Key1 = Base64.getEncoder().encodeToString(key1.getEncoded());
        System.out.println("Generated Key (Base64): " + base64Key1);

        // 2. 직접 정의한 문자열을 키로 변환
        String str = "비밀번호입니다64자이상되어야합니다비밀번호입니다64자이상되어야합니다";
        SecretKey key2 = Keys.hmacShaKeyFor(str.getBytes());
        String base64Key2 = Base64.getEncoder().encodeToString(key2.getEncoded());
        System.out.println("Custom Key (Base64): " + base64Key2);
    }
}