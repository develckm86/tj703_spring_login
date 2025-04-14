package com.tj703.l09_spring_login.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserValid {
    @NotBlank(message = "아이디는 꼭 입력해야합니다.")
    private String id;
    @Size(min = 4, max = 8)
    private String pw;
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,8}$")
    private String name;
    //@Pattern(regexp = "^(ADMIN|USER|MANAGER|GUEST)$")
    private String role;
}