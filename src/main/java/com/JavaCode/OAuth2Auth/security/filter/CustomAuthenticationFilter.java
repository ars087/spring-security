package com.JavaCode.OAuth2Auth.security.filter;


import com.JavaCode.OAuth2Auth.model.User;
import com.JavaCode.OAuth2Auth.model.enam.Role;
import com.JavaCode.OAuth2Auth.repository.IUserRepository;
import com.JavaCode.OAuth2Auth.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Component
public class CustomAuthenticationFilter extends SimpleUrlAuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);
    private final IUserRepository userRepository;
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final RefreshTokenService tokenService;

    public CustomAuthenticationFilter(IUserRepository userRepository,
                                      OAuth2AuthorizedClientService authorizedClientService,
                                      RefreshTokenService tokenService) {
        this.userRepository = userRepository;
        this.authorizedClientService = authorizedClientService;
        this.tokenService = tokenService;
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User currentUser = (OAuth2User) authentication.getPrincipal();
        String email = currentUser.getAttribute("email");
        String name = currentUser.getAttribute("name");
        OAuth2AuthenticationToken oauth2Auth = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauth2Auth.getAuthorizedClientRegistrationId();

        try {

            User user = userRepository.findByEmail(email).orElseGet(() -> {
                User newUser = new User();
                newUser.setUserName(name);
                newUser.setEmail(email);
                newUser.setRole(Role.ADMIN);
                return userRepository.save(newUser);
            });

            OAuth2AuthenticationToken newAuth = new OAuth2AuthenticationToken(
                currentUser, List.of(new SimpleGrantedAuthority(user.getRole().toString())), registrationId);
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            logger.info("Aутентификация для пользователя: {}", request.getRequestURI());

            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                registrationId, authentication.getName());
            if (authorizedClient != null) {
                Instant expiresAt = authorizedClient.getAccessToken().getExpiresAt();
                if (expiresAt != null && expiresAt.isBefore(Instant.now())) {
                    String errorMessage = "Access token has expired. Please reauthenticate.";
                    logger.warn("токен просрочен :{}", errorMessage);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write(errorMessage);
                    response.getWriter().flush();
                    return;
                }
            }

            if (authorizedClient.getRefreshToken() != null) {
                String refreshToken = authorizedClient.getRefreshToken().getTokenValue();
                tokenService.saveRefreshToken(email, refreshToken);
                logger.info("Рефреш токен: сохранён");
            }


        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Ошибка при аутентификации";
            logger.warn("Ошибка аутентификации :{}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(errorMessage);
            response.getWriter().flush();

            return;
        }

//        SavedRequest savedRequest = requestCache.getRequest(request, response);
//
//        if (savedRequest != null) {
//                String targetUrl = savedRequest.getRedirectUrl();
//            getRedirectStrategy().sendRedirect(request, response, targetUrl);
//        } else {
//
//            super.onAuthenticationSuccess(request, response, authentication);
//        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
