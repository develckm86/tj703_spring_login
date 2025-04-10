package com.tj703.l09_spring_login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {
    @GetMapping("/list.do")
    public String list(){
        //USER 권한 이상만 조회가능 (로그인이 안되어 있거나 GUEST 는 조회 불가능)
        return "board/list";// template/board/list.html 을 렌더링 하겠다.
    }
}
