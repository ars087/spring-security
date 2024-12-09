package TestJavaCode.AuthApp.repository;

import TestJavaCode.AuthApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String username);

    @Query("SELECT u FROM User u")
    Optional<List<User>> getAllByUser();

}
