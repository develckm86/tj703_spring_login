package com.tj703.l09_spring_login.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter
@ToString
public class UserLoginValid {
    @NotBlank
    private String id;
    @NotBlank
    private String pw;
}