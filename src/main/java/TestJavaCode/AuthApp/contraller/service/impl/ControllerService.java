package TestJavaCode.AuthApp.contraller.service.impl;

import TestJavaCode.AuthApp.repository.ITokenRepository;
import TestJavaCode.AuthApp.repository.IUserRepository;
import TestJavaCode.AuthApp.security.filter.LoggingFilter;
import TestJavaCode.AuthApp.security.service.OurUserDetailedService;
import TestJavaCode.AuthApp.contraller.service.IControllerService;
import TestJavaCode.AuthApp.exception.AccountBlockException;
import TestJavaCode.AuthApp.exception.CustomBadCredentialsExceptions;
import TestJavaCode.AuthApp.exception.CustomExceptionNotFound;
import TestJavaCode.AuthApp.model.User;
import TestJavaCode.AuthApp.dto.requestDTO.AuthLoginPasswordRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
@Service
@AllArgsConstructor
public class ControllerService implements IControllerService {

    private final AuthenticationManager authenticationManager;

    private final OurUserDetailedService userDetailsService;

    private final ITokenRepository iTokenRepository;
    private final LoggingFilter loggingFilter;

    @Override
    public void blockUser(AuthLoginPasswordRequestDTO authRequest) {

        String userName = authRequest.getUsername();
        User user = userDetailsService.findByUsername(userName)
            .orElseThrow(() -> new CustomExceptionNotFound("Пользователь не найден"));
        if (!user.isAccountNonLocked()) {
           loggingFilter.attemptingToAuthenticate(userName);
            throw new AccountBlockException("Доступ заблокирован");

        } else {
            try {
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName, authRequest.getPassword()));
            } catch (BadCredentialsException e) {
                   loggingFilter.validatePasswordWithLogging(userName);
                if (user.getFailedAttempts() < 5) {
                    user.incrementFailedAttempts();
                    userDetailsService.save(user);

                } else {
                    user.setAccountNonLocked(false);
                    userDetailsService.save(user);
                    loggingFilter.logAccountLock(userName);
                    throw new AccountBlockException("Доступ заблокирован.Обратитесь к администратору!");
                }

                throw new CustomBadCredentialsExceptions(e.getMessage());
            }

        }
        if (user.getFailedAttempts() > 0) {
            user.resetFailedAttempts();
            userDetailsService.save(user);
        }

    }

}
