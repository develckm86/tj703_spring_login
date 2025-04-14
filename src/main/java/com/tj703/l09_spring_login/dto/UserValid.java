package com.tj703.l09_spring_login.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tj703.l09_spring_login.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserValid {
    @Size(min = 4,max = 8,message = "아이디는 4개이상 8이하 작성하셔야합니다.")
    private String id;
    @JsonIgnore
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])[A-Za-z[^A-Za-z]]{4,12}$"
            ,message = "비밀번호는 4~8 영어로 작성하고 대문자 포함"
    )
    private String pw;

    private String role;
    @Pattern(regexp = "^[가-힣]{2,8}$",message = "이름은 한글만 입력가능 2~8")
    private String name;
}