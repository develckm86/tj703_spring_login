package com.tj703.l09_spring_login.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter@Setter@ToString
public class SignupVaild {
    @NotBlank
    private String id;
    @NotNull
    private MultipartFile pictureFile;
    @NotBlank
    private String name;
    @NotBlank
    private String pw;
    @NotBlank
    private String pwRe;
}
