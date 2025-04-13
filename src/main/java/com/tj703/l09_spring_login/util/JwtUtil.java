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

/*
* 쿠키 기반 JWT 인증 (현재 사용 중)
장점:
브라우저에서 자동으로 처리: JWT를 쿠키에 저장하고, 서버에서 자동으로 JWT를 읽어 처리하므로 클라이언트에서는 명시적으로 토큰을 관리할 필요가 없습니다.
편리한 인증 유지: JWT가 쿠키에 저장되면, 세션이 끊어지지 않는 한 클라이언트가 브라우저를 새로고침해도 자동으로 로그인 상태를 유지할 수 있습니다.
보안 처리 가능: HttpOnly, Secure, SameSite 옵션을 사용하여 쿠키를 안전하게 설정할 수 있습니다.
단점:
크로스 도메인 문제: 쿠키는 기본적으로 같은 도메인에서만 유효하므로, 서버와 클라이언트가 다른 도메인에 있을 경우 CORS 문제나 쿠키 전송 문제가 발생할 수 있습니다.
XSS 취약점: HttpOnly 설정을 통해 XSS 공격을 완화할 수 있지만, JWT가 클라이언트에 노출되므로 XSS 공격에 민감할 수 있습니다.

JWT + LocalStorage / SessionStorage 방식
JWT를 쿠키가 아닌, 브라우저의 localStorage 또는 sessionStorage에 저장하는 방법도 있습니다.
장점:
세션 독립적: 로컬 스토리지에 저장된 JWT는 서버와 독립적으로 클라이언트에서 관리되므로, 쿠키의 세션 정보에 의존하지 않습니다.
크로스 도메인: 클라이언트에서 직접 헤더에 토큰을 추가할 수 있기 때문에, 서버와 클라이언트가 다른 도메인에 있어도 문제가 발생하지 않습니다.
직관적: localStorage에 JWT를 저장하고, 클라이언트에서 명시적으로 Authorization 헤더에 토큰을 첨부하여 서버로 요청을 보낼 수 있습니다.
단점:
보안 이슈: localStorage나 sessionStorage에 저장된 데이터는 XSS 공격에 노출될 수 있습니다. 토큰이 노출되면 악의적인 공격자가 이를 탈취하여 사용할 수 있습니다.
서버와의 상호작용: 클라이언트에서 명시적으로 Authorization 헤더를 추가해야 하므로, 클라이언트 코드에서 매번 토큰을 관리하고 헤더에 포함시켜야 하는 번거로움이 있습니다.

*/
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
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username) //토큰 식별자로 사용자 아이디 사용
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
