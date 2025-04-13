package com.tj703.l09_spring_login.controller;

import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("/user/list.do")
    public ResponseEntity<List<User>> userList() {
        return ResponseEntity.ok(userService.findAll());
    }

}
