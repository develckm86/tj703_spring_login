package com.tj703.l09_spring_login.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 {email:"develckm86@gmail.com"
family_name:"최"
given_name:"경민"
name:"최경민"
picture:"https://lh3.googleusercontent.com/a/ACg8ocKFIyoCgkOgUm_7TogXewM8YpZQX99VzDrhmh4kNWvYFACQ7jY=s96-c"}*/
@Getter@Setter@ToString
public class GoogleUser {
    String email;
    @JsonProperty("family_name")
    String familyName;
    @JsonProperty("given_name")
    String givenName;
    String name;
    String picture;
}
