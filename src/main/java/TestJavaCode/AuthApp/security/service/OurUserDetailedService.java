package TestJavaCode.AuthApp.security.service;

import TestJavaCode.AuthApp.model.User;
import TestJavaCode.AuthApp.repository.ITokenRepository;
import TestJavaCode.AuthApp.repository.IUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OurUserDetailedService implements UserDetailsService {


    private final IUserRepository userRepository;
    private  final ITokenRepository tokenRepository;

    public OurUserDetailedService(IUserRepository userRepository, ITokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUserName(username);
    }
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user =  findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
            List.of(new SimpleGrantedAuthority(user.getRole().toString())));
    }

    public void save(User user) {
        userRepository.save(user);
    }
    public void upDataToken(String token, String userName ){
        tokenRepository.updateTokenByUsername(token,userName);
    }

}