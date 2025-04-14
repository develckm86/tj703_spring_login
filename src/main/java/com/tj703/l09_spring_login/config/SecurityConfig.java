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

@Configuration
@EnableWebSecurity //spring-security 가 활성화 될때 같이 활성화
@AllArgsConstructor
public class SecurityConfig {
    private  final CustomUserDetailsService customUserDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        //처음 제공하는 로그인 양식에는 크로스사이트요청 위조(scrf Cross-Site Request Forgery) 방지용 고유값이 존재하는데
        // -> 카카오사이트 네이버로 양식제출해서 로그인
        // name="_csrf" value="12323fsa1233sdggdww"
        // /user/login.do 양식에는 고유값이 없어서
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(autu-> autu
//                        .requestMatchers(HttpMethod.POST,"/login").permitAll()
                        .requestMatchers(
                                "/",
                                "/public/**", // /css/** or /js/** or /img/** ..
                                "/favicon.ico",
                                "/user/login.do"

                        ).permitAll() //요청허용
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN","MANAGER")
                        //role ADMIN 인 유저면 /admin/** 요청할 수 있다. 아니면 403
                        .anyRequest().authenticated() //모든 요청을 로그인체크하겠다.
                )
                .formLogin(form->form
                        .loginPage("/user/login.do")
                        // id-> username pw -> password 로 input의 name을 변경
                        // spring security 가 자동으로 로그인액션 페이지를 생성하는데
                        // url이 from의 url과 같다.
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
