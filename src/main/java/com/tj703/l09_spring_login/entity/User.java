package com.tj703.l09_spring_login.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor //빌더 디자인패턴 (생성)
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "pw", nullable = false)
    private String pw;

    @Lob
    @Column(name = "role")
    private String role;

    @Column(name = "name", nullable = false)
    private String name;

}