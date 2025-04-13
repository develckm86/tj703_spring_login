package com.tj703.l09_spring_login.config;

import com.tj703.l09_spring_login.filter.JwtCookieLoginFilter;
import com.tj703.l09_spring_login.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //스프링 security 설정 활성화
@AllArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtCookieLoginFilter jwtCookieLoginFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //CSRF는 Cross-Site Request Forgery (크로스 사이트 요청 위조)
        //다른 사이트에 양식을 재출하도록 유도하는 공격
        //name="_csrf" value="z8djf8sd9fjs9fj9jfdskf" 이런 고유값을 생성해 넣으면
        //다른 사이트에서 요청이 온것인지 확인 가능
        return http
                .csrf(AbstractHttpConfigurer::disable)//csrf 제외
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//세션을 사용하지 않겠다.
                .authorizeHttpRequests(auth->auth
                        .requestMatchers(HttpMethod.POST, "/user/api/login.do").permitAll()
                        .requestMatchers(
                                "/","/index.html",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "MANAGER")
                        .anyRequest().authenticated()//나머지 요청은 로그인 인증을 사용, 로그인 안되어 있거나 권한이 없으면 403 에러
                )
                .addFilterBefore(jwtCookieLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    //로그인시 어떻게 할건지 동작정의 (UserDetailsService 구현 및 passwordEncoder 작성)
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();

    }
    //authenticationManager에서 로그인시 비밀번호 인코딩 방식 정의
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
