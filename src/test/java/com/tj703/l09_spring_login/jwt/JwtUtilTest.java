package com.tj703.l09_spring_login.jwt;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;
    @Test
    void generateToken() {
        String token = jwtUtil.generateToken("user1");
        System.out.println(token);
        //eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTc0NDY4ODc4NH0.wS2axiyKR5s3ZBTJFCjJsD8bpgye8nzfRbEaBlBiQHxr9jpZWWVaNb-8IFEFySIR80yesSPPPmkDBBbpzobz9Q
    }

    @Test
    void validateToken() throws JwtException {
        String token="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTc0NDY4ODc4NH0.wS2axiyKR5s3ZBTJFCjJsD8bpgye8nzfRbEaBlBiQHxr9jpZWWVaNb-8IFEFySIR80yesSPPPmkDBBbpzobz9Q";
        boolean check=jwtUtil.validateToken(token);
        assertTrue(check);
    }

    @Test
    void getUsername() {
        String token=jwtUtil.generateToken("user1");
        String username= jwtUtil.getUsername(token);
        System.out.println(username); //"user1" 이면 성공
        assertEquals(username,"user1");
    }
}