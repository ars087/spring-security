package com.javaCode.SSTaskOne;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/home").authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults())
            //       .csrf().disable(); // Отключаем CSRF (для упрощения тестов)
            .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var userDetailsManager = new InMemoryUserDetailsManager();


        userDetailsManager.createUser(
            User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
//                .roles("USER") // Роль пользователя
                .build()
        );

        return userDetailsManager;
    }
}