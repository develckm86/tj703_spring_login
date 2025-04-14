package com.tj703.l09_spring_login.config;

import com.tj703.l09_spring_login.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;

@Configuration
@EnableWebSecurity //spring-security 가 활성화 될때 같이 활성화
@AllArgsConstructor
public class SecurityConfig {
    private  final CustomUserDetailsService customUserDetailsService;
    @Bean//SecurityFilterChain : 로그인 인증하는 필터
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //CSRF는 Cross-Site Request Forgery (크로스 사이트 요청 위조)
        //다른 사이트에 양식을 재출하도록 유도하는 공격
        //name="_csrf" value="z8djf8sd9fjs9fj9jfdskf" 이런 고유값을 생성해 넣으면
        //다른 사이트에서 요청이 온것인지 확인 가능
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth
                        .requestMatchers(HttpMethod.POST,"/user/signup.do").permitAll()
                        .requestMatchers(
                                "/user/signup.do",
                                "/","index.html",
                                "/public/**",
                                "/favicon.ico"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN","MANAGER")
                        .anyRequest().authenticated() //모든 요청을 로그인 인증하겠다.
                )
                .formLogin(form->form
                        .loginPage("/user/login.do")
                        .failureUrl("/user/login.do?error=true")
                        //자동으로 post /user/login.do 가 생성됨(username,password)
                        .permitAll()
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    //login 시도시 평문을 BCrypt 로 비교
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
