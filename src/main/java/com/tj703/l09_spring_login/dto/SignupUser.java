package com.tj703.l09_spring_login.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter@Setter@ToString
public class SignupUser {
    @NotBlank
    private String id;
    @NotBlank
    private String name;
    private MultipartFile pictureFile;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,16}$")
    private String pw;
}
