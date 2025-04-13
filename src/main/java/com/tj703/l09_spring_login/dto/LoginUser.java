package com.tj703.l09_spring_login.dto;

import com.tj703.l09_spring_login.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public class LoginUser {
    private final User user;
    public LoginUser(User user) {
        this.user = user;
        this.id=user.getId();
        this.name=user.getName();
    }
    private String id;
    private String name;
}
