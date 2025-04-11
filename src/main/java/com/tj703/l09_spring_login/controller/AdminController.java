package com.tj703.l09_spring_login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/board/list.do")
    public void boardList() {} //void : url과 동일한 위치의 html 렌더링

    @GetMapping("/user/list.do")
    public void userList() {}
}
