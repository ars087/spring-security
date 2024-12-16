package com.JavaCode.OAuth2Auth.repository;

import com.JavaCode.OAuth2Auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, String> {
    @Query("SELECT  u FROM User  u   WHERE u.email = ?1")
    Optional<User> findByEmail(String email);


    Optional<User> findByUserName(String username);
}
