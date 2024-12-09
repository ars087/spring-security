package TestJavaCode.AuthApp.controller;


import TestJavaCode.AuthApp.contraller.service.IControllerService;
import TestJavaCode.AuthApp.dto.requestDTO.AuthLoginPasswordRequestDTO;
import TestJavaCode.AuthApp.dto.requestDTO.TokenRefreshRequestDTO;
import TestJavaCode.AuthApp.dto.responseDTO.AuthAllTokenResponseDTO;
import TestJavaCode.AuthApp.dto.responseDTO.TokenRefreshResponseDTO;
import TestJavaCode.AuthApp.exception.TokenRefreshException;
import TestJavaCode.AuthApp.repository.ITokenRepository;
import TestJavaCode.AuthApp.security.filter.LoggingFilter;
import TestJavaCode.AuthApp.security.service.OurUserDetailedService;
import TestJavaCode.AuthApp.security.utils.JWTUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoggingFilter loggingFilter;
    private final OurUserDetailedService userDetailsService;
    private final IControllerService controllerService;
    private final JWTUtils jwtUtil;
    private final ITokenRepository tokenRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login( @RequestBody AuthLoginPasswordRequestDTO authRequest) {
        if (authRequest == null) {
            return ResponseEntity.badRequest().body("отсутствуют данные  логина либо пороля");
        }
        String userName = authRequest.getUsername();
        controllerService.blockUser(authRequest);
        AuthAllTokenResponseDTO authResponseDTO = new AuthAllTokenResponseDTO();

        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        authResponseDTO.setTokenAccess(jwtUtil.generateAccessToken(userDetails));
        authResponseDTO.setTokenRefresh(jwtUtil.generateRefreshToken(userDetails));
        loggingFilter.logJwtGeneration(userName);
        loggingFilter.logSuccessfulLogin(userName,"пользователь прошел успешную аутентификацию");
        userDetailsService.upDataToken(authResponseDTO.getTokenRefresh(), userName);
        return ResponseEntity.ok().body(authResponseDTO);
    }


    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequestDTO requestToken) {

        String userName = jwtUtil.extractUsername(requestToken.getRefreshToken());
        System.out.println(userName);
        return tokenRepository.findTokenByUserName(userName)
            .filter(token -> !jwtUtil.isTokenExpired(token.getToken())) // Фильтруем по состоянию токена
            .map(token -> {
                String username = jwtUtil.extractUsername(token.getToken());
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                String accessToken = jwtUtil.generateAccessToken(userDetails);
                return ResponseEntity.ok(new TokenRefreshResponseDTO(accessToken, token.getToken()));
            }).orElseThrow(() -> new TokenRefreshException(requestToken.getRefreshToken(), "sdfdfs"));

    }


}
