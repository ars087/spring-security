package TestJavaCode.AuthApp.security.config;

import TestJavaCode.AuthApp.security.filter.JwtAuthenticationFilter;
import TestJavaCode.AuthApp.security.filter.LoggingFilter;
import TestJavaCode.AuthApp.security.service.OurUserDetailedService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OurUserDetailedService ourUserDetailedService;
    private final LoggingFilter loggingFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          OurUserDetailedService ourUserDetailedService, LoggingFilter loggingFilter


    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.ourUserDetailedService = ourUserDetailedService;
        this.loggingFilter = loggingFilter;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        return http.csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                //   .requestMatchers("/api/v1/auth/login").permitAll()
                //  .requestMatchers("/api/v1/auth/refreshtoken").permitAll()

                .requestMatchers("/api/v1/app/**").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll())

//                  ).requiresChannel(channel -> channel
//              .requestMatchers("/api/v1/auth/refreshtoken").requiresSecure()
//              .anyRequest().requiresInsecure())
//                .requestMatchers("/api/v1/app/work").hasAnyAuthority("USER"))
//                .requestMatchers("/api/v1/app/**")
//                .authenticated())

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

            .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(ourUserDetailedService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

    }
}