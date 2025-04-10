package com.tj703.l09_spring_login.repository;

import com.tj703.l09_spring_login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByIdAndPw(String id, String pw);

}
