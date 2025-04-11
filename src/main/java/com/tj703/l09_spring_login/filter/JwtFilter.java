package com.tj703.l09_spring_login.filter;

import com.tj703.l09_spring_login.dto.CustomUserDetails;
import com.tj703.l09_spring_login.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    //요청이온 토큰 중에 jwt 가 있는지 확인하고 인증하는 필터
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //요청해더에서 Authorization 만 추출(토큰이 존재)
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            //Bearer 토큰이 JWT임
            String token = header.substring(7); //"Bearer " 제거
            if(jwtUtil.validateToken(token)){ //토큰이 유요한 서명인지 확인
                String username = jwtUtil.getUsername(token); //토큰에 담긴 유저 확인
                //토큰에 저장된 아이디로 다시 유저 조회
                CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
                //spring security가 로그인 인증 때 사용함 (강제 로그인)
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                        userDetails,                // Principal (인증된 사용자 정보)
                        null,                       // Credential (보통은 비밀번호인데, 인증 후엔 null로 처리)
                        userDetails.getAuthorities()); // 권한 목록 (ROLE_USER, ROLE_ADMIN 등)
                //인증 검사에 사용할 정보로 저장
                SecurityContextHolder.getContext().setAuthentication(authToken);


            }

        }

    }
}
