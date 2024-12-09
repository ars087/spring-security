package TestJavaCode.AuthApp.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class HttpsRedirectFilter extends OncePerRequestFilter {

    private static final String HTTPS_PREFIX = "https://";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {


        if (request.getScheme().equals("http")) {
            String requestedUri = request.getRequestURI();

            if (shouldRedirectToHttps(requestedUri)) {

                String newUrl = HTTPS_PREFIX + request.getServerName() + ":" + request.getServerPort() + requestedUri;
                response.sendRedirect(newUrl);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldRedirectToHttps(String uri) {

        return uri.startsWith("/refresh");// || uri.startsWith("/login");
    }




}
