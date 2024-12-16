package com.JavaCode.OAuth2Auth.repository;

import com.JavaCode.OAuth2Auth.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    // Удаление токенов по email пользователя
    void deleteByUser_Email(String email);

    // Поиск токена по email пользователя
    Optional<RefreshToken> findByUser_Email(String email);
}
