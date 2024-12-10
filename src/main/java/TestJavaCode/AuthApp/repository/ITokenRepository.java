package TestJavaCode.AuthApp.repository;

import TestJavaCode.AuthApp.model.Token;
import TestJavaCode.AuthApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ITokenRepository extends JpaRepository<Token, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Token u SET u.token = ?1 WHERE u.user.userName = ?2")
    void updateTokenByUsername(String token, String userName);


    @Query("SELECT u FROM  Token u WHERE u.user.userName = ?1")
    Optional<Token> findTokenByUserName(String username);

}
