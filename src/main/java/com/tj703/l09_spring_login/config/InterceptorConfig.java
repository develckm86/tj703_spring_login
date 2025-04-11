package com.tj703.l09_spring_login.config;

import com.tj703.l09_spring_login.interceptor.LoginCheckInterceptor;
import com.tj703.l09_spring_login.interceptor.ManagerCheckInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  //설정 컴포넌트 : 스프링이 최초로 실행될 때 설정으로 지정됨
@AllArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {
    private final LoginCheckInterceptor loginCheckInterceptor;
    private final ManagerCheckInterceptor managerCheckInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/board/**");
        registry.addInterceptor(managerCheckInterceptor)
                .addPathPatterns("/admin/**");
    }
}
