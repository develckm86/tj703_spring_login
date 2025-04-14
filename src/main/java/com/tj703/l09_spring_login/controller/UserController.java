package com.tj703.l09_spring_login.controller;

import com.tj703.l09_spring_login.dto.CustomUserDetails;
import com.tj703.l09_spring_login.dto.UserValid;
import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(defaultValue = "false") boolean error,
            Model model) {
            if(error) {
                model.addAttribute("error", "아이디와 비밀번호를 확인하세요.");
            }
            return "user/login";
    }
    @GetMapping("/signup.do")
    public String signup(
            Model model,
            UserValid user
    ){
        model.addAttribute("userValid", user);
        return "user/signup";
    }
    @PostMapping("/signup.do")
    public String signupAction(
            @Valid @ModelAttribute UserValid user,
            BindingResult  bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            return "user/signup"; //자동으로 UserValid user 를 "userValid"로 렌더링하는 페이지에 바이딩
        }
        User userEntity=User.builder()
                .id(user.getId())
                .pw(user.getPw())
                .name(user.getName())
                .build();

        try{
            userService.guestSignup(userEntity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return "redirect:/";
    }
}
