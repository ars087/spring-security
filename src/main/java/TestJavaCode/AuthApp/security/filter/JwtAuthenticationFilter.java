package TestJavaCode.AuthApp.security.filter;

import TestJavaCode.AuthApp.security.service.OurUserDetailedService;
import TestJavaCode.AuthApp.security.utils.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final OurUserDetailedService userDetailedService;
     private  final  LoggingFilter loggingFilter;
    public JwtAuthenticationFilter(JWTUtils jwtUtils, OurUserDetailedService userDetailedService,
                                   LoggingFilter loggingFilter) {
        this.jwtUtils = jwtUtils;

        this.userDetailedService = userDetailedService;
        this.loggingFilter = loggingFilter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)

        throws ServletException, IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        final Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("path", request.getServletPath());


        String authHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);


            try {
                username = jwtUtils.extractUsername(token);

            } catch (ExpiredJwtException ex) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                errorResponse.put("message",  ex.getMessage()+ " Токен просрочен");
                response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
               loggingFilter.logExpiredJwt();
              loggingFilter.logFailedLogin(ex.getMessage());
                return;
            } catch (JwtException exception) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                errorResponse.put("message", exception.getMessage());
                response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
                loggingFilter.logJwtIsBroken();
                loggingFilter.logFailedLogin(exception.getMessage());
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailedService.loadUserByUsername(username);

                if (jwtUtils.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    loggingFilter.logSuccessfulLogin(username,request.getRequestURI());
                }
            }

        }
        filterChain.doFilter(request, response);
    }

}
