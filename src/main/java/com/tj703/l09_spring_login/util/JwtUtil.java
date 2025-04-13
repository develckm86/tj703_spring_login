package com.tj703.l09_spring_login.util;

import com.tj703.l09_spring_login.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.Signature;
import java.util.Date;

@Component
public class JwtUtil {
    //토큰 서명용 비밀 키
    private final String SECRET_KEY = "비밀번호입니다21자이상되어야합니다.";
    //토큰 유효시간
    private  final SecretKey secretKey;
    public JwtUtil() {
        this.secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    private final long EXPIRATION = 1000*60*30; //30분
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getId()) //토큰 식별자로 사용자 아이디 사용
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION)) //토큰 만료시간
                //.signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .signWith(secretKey)//토큰 서명 (암호화)
                .compact();
    }
    /*Header(토큰정보).Payload(본문,데이터).Signature (서명)
    *Payload:{
  "sub": "kyeongmin",         // subject (클레임)
  "role": "ADMIN",            // 사용자 권한 (클레임)
  "exp": 1712956821,          // 만료 시간 (클레임)
  "iat": 1712953221           // 발급 시간 (클레임)
}*/
    // 토큰에서 username 꺼내기
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) //토큰의 서명을 검증한 비밀키 설졍(검증)
                .build() //설정 기반으로 Jwt 파서 객체 생성
                .parseSignedClaims(token) //Claims : 토큰에 담긴 정보 조각
                .getPayload() //객체를 가져옴
                .getSubject();//서명할때 고유값으로 지정한 id 조회

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseClaimsJws(token);
            logger.info("유요한 서명");
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {

            logger.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {

            logger.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {

            logger.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {

            logger.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;    }
}
