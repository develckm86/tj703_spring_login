package com.tj703.l09_spring_login.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
@Builder //생성자 규칙이 복작할때 사용하는 디자인패턴
public class User {
    public User() {}
    //public User(String id, String pw) {}
//    public User(String id, String pw, String name) {
//        this.id = id;
//        this.pw = pw;
//        this.name = name;
//    }
//    public User(String id,String name){
//        this.id = id;
//        this.name = name;
//    }
//    public User(String id, String pw, String name,String role) {
//        this.id = id;
//        this.pw = pw;
//        this.name = name;
//    }
//


    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @JsonIgnore
    @Column(name = "pw", nullable = false)
    private String pw;

    @Lob
    @Column(name = "role")
    private String role;

    @Column(name = "name", nullable = false)
    private String name;

}