package com.tj703.l09_spring_login.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
@Component
public class User {
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