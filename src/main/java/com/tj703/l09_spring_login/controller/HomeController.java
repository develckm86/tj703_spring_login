package com.tj703.l09_spring_login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
//        return "index"; template/index.html
        return "forward:index.html";   //static/index.html
    }
}
