package com.tj703.l09_spring_login.interceptor;

import com.tj703.l09_spring_login.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ManagerCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute("loginUser");
        if (userObj == null ) {
            response.sendRedirect("/user/login.do");
            return false;
        }else {
            User user = (User) userObj;
            String role=user.getRole();
            if(role.equals("GUEST") || role.equals("USER")) {
                response.sendRedirect("/");
                return false;
            }
        }
        return true;
    }
}
