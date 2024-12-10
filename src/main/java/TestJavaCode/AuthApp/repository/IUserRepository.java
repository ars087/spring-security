package TestJavaCode.AuthApp.repository;

import TestJavaCode.AuthApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String username);

    @Query("SELECT u FROM User u")
    Optional<List<User>> getAllByUser();

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isAccountNonLocked = ?1, u.failedAttempts =?2 WHERE u.userName = ?3")
    void blockingStatusUpdate(boolean isAccountNonLocked, int number,  String userName);

    @Query("SELECT u.isAccountNonLocked FROM  User u WHERE u.userName = ?1")
    Optional<Boolean> getBlockingStatus(String userName);


}
