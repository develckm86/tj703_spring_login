package com.tj703.l09_spring_login.controller;

import com.tj703.l09_spring_login.dto.UserValid;
import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @GetMapping("/login.do")
    public String login(
             HttpSession session,
             @SessionAttribute(required = false) User loginUser
             //required=true 자동으로 로그인 한 사람만 이 리소스 요청가능(400)
    ) {
        //로그인되어 있는 유저는 다시 로그인 폼으로 올 수 없다.
        //Object loginUserObj=session.getAttribute("loginUser");
        if(loginUser==null) {
            return "user/login";
        }else{
            return "redirect:/";
        }

    }
    // /logout  로그아웃 액션이 구현되어 있음
    // user/login.do 가 자동 생성
//    @PostMapping("/login.do")
//    public String loginAction(
//            @ModelAttribute User user,
//            HttpSession session
//            ) {
//        //DTO로 id, pw 파라미터를 파싱
//        //로그인  : 1.db 에 저장된 유저가 id/pw 가 같은 사람이 존재
//        //2. 만약 존재한다면 서버에 계속 접속하고 있는 척 해야합니다.
//        //3. http 통신은 요청 응답을 하기 때문에 접속을 유지할 수 없다.
//        //=> 때문에 서버에서 유지하는 객체인 세션을 만들어서 접속하는 척 합니다.
//        //4. 만약 세션 객체를 만든 클라이언트가 다시 접속하면 서버가
//        //클라이언트가 생성한 세션 객체를 요청한 동적리소스에 같이 제공
//        Optional<User> loginUserOpt=userService.login(user.getId(),user.getPw());
//        if(loginUserOpt.isEmpty()){
//            return "redirect:/user/login.do";
//        }else{
//            User loginUser=loginUserOpt.get();
//            session.setAttribute("loginUser",loginUser);
//            return "redirect:/";
//        }
//
//    }
    @GetMapping("/logout.do")
    public String logoutAction( HttpSession session ) {
        session.removeAttribute("loginUser");
        //session.invalidate(); //만료시간을 다되게해서 삭제 (모든 세션 삭제)
        //session 객체는 서버에서 기본 30분 유지
        return "redirect:/";
    }
    @GetMapping("/signup.do")
    public String signup(
            Model model,
            UserValid user) {
        model.addAttribute("userValid", user);
        return "user/signup";
    }
    @PostMapping("/signup.do")
    public String signup(
            @Valid @ModelAttribute UserValid user,
            BindingResult bindingResult,
            HttpServletResponse response,
            Model model
    ) throws IOException {
        //ModelAttribute or RequestBody 로 dto를 파싱할때 검증 @Valid
        if(bindingResult.hasErrors()) {
            //model.addAttribute("userValid", user); //자동으로 추가됨
            //model 에 더한 객체는 redirect 로 전달 불가
            //오류가 있으면 렌더링하는 페이지에 user 가 전달됨
            return "/user/signup";
        }
        try {
//            User userEntity=new User();
//            userEntity.setPw(user.getPw());
//            userEntity.setName(user.getName());
//            userEntity.setId(user.getId());
            User userEntity= User.builder()
                    .pw(user.getPw())
                    .name(user.getName())
                    .id(user.getId())
                    .build();


            userService.guestSignup(userEntity);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            logger.error(e.getMessage());
            //response.sendError(409);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            //response.sendError(500);
        }
        return  "redirect:/";
    }



}
