package com.tj703.l09_spring_login.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    // /board/list.do -> 동적 리소스 요청

    // 동적리소스 요청 전에 preHandle 실행
    //요청 전에 인증의 용도
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        System.out.println("preHandle");
        HttpSession session = request.getSession();
        Object userObj=session.getAttribute("user");
        if(userObj==null) {
            response.sendRedirect("/");
            return false; //동적리소스 요청을 하지 않겠다.
        }
        return true; //원래 요청하던 동적리소스로 요청
    }
    // /board/list.do 동적 리소스가 실행되자 마자 postHandler 실행
    // ModelAndView modelAndView : 렌더링할 뷰를 선택하거나 뷰에서 렌더링할 객체를 전달
    // Model.addAttribute("emp",emp)
    // 컨트롤러가 정상 실행된 후 View 를 어떻게 렌더링할 건지 제어 (사용자 역할에 따라 뷰변경,뷰에 공통 데이터 전달)
    // 인증 인가로 뷰를 바꾸기보단 preHandle 다른 뷰로 이동(302)하는 것이 더 좋다.
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //modelAndView.addObject("role","유저");
        //System.out.println("postHandle");
    }
    // /board/list.do 동적 리소스가  뷰를 모두 렌더링하고 응답할때 실행
    // 예외 처리용
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //System.out.println("afterCompletion");
    }
}
