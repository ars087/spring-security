package com.JavaCode.OAuth2Auth.cofig;

import com.JavaCode.OAuth2Auth.exception.CustomAccessDeniedHandler;
import com.JavaCode.OAuth2Auth.exception.CustomAuthenticationEntryPoint;
import com.JavaCode.OAuth2Auth.security.filter.CustomAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationFilter customAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfiguration(
        CustomAccessDeniedHandler customAccessDeniedHandler,
        CustomAuthenticationFilter customAuthenticationFilter,
        CustomAuthenticationEntryPoint customAuthenticationEntryPoint

    ) {
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAuthenticationFilter = customAuthenticationFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth ->
                auth
                    .requestMatchers("/", "/login", "/error", "/webjars").permitAll()
                    .requestMatchers("/h2-console", "/home").permitAll()
                    .requestMatchers("/admin").hasAuthority("ADMIN")
                    .requestMatchers("/s_admin").hasAuthority("SUPER_ADMIN")
                    .anyRequest().authenticated())
            .exceptionHandling(exception -> exception
                .accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(customAuthenticationEntryPoint)
            )
            .oauth2Login(oauth2Login -> oauth2Login
                .successHandler(customAuthenticationFilter)
            ).build();

    }
}
