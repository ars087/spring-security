package com.JavaCode.OAuth2Auth.service;

import com.JavaCode.OAuth2Auth.model.RefreshToken;
import com.JavaCode.OAuth2Auth.repository.IRefreshTokenRepository;
import com.JavaCode.OAuth2Auth.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

    private final IRefreshTokenRepository refreshTokenRepository;
    private final IUserRepository userRepository;

    public RefreshTokenService(IRefreshTokenRepository refreshTokenRepository, IUserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public void saveRefreshToken(String email, String refreshToken) {

        RefreshToken refreshTokenEntity = refreshTokenRepository
            .findByUser_Email(email)
            .orElseGet(() -> {

                RefreshToken newToken = new RefreshToken();
                newToken.setUser(userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден")));
                return newToken;
            });

        refreshTokenEntity.setToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }

}
