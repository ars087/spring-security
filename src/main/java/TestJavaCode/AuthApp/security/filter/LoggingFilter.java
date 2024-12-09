package TestJavaCode.AuthApp.security.filter;

import TestJavaCode.AuthApp.logger.LoggerService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private final LoggerService loggerService;

    public LoggingFilter(LoggerService loggerService) {
        this.loggerService = loggerService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {


        String requestURI = request.getRequestURI();
        loggerService.logAction("Попытка входящего  запроса:", request.toString(), requestURI);

        System.out.println(response.getStatus());

        filterChain.doFilter(request, response);

   }

    public void logSuccessfulLogin(String userName, String requestPath) {

        loggerService.logAction(userName, "аутентификация успешная  ", requestPath);
    }

    public void logFailedLogin(String userName) {

        loggerService.logAction(userName, "Аутентификация не удалась"," ошибка  валидации токена");
    }

    public void logJwtGeneration(String userName) {

        loggerService.logAction(userName, "JWT сгенерированы", "JWT сгенерирован и отправлен пользователю");
    }

    public void attemptingToAuthenticate(String userName){

      loggerService.logAction(userName,"ошибка доступа","попытка аутентификации заблокированного пользователя");
    }

    public void logJwtIsBroken() {

        loggerService.logAction("JWT не действителен","Ошибка при проаерки токена" ,
            "JWT либо поврежден , либо принадлежит ни этому серверу");

    }


   public  void   validatePasswordWithLogging(String userName){
       loggerService.logAction(userName,"Ошибка при попытке  аутентифицироваться " ,
           "Пользователь вводит не верный пароль");


   }

    public void logAccountLock(String username) {

        loggerService.logAction(username, "Аккоунт заблокирован",
            "Аккаунт заблокирован после нескольких неудачных попыток входа в систему");
    }

    public void logExpiredJwt () {

        loggerService.logAction("JWT просрочен", "",
            "Необходимо обновление токена ");
    }

}
