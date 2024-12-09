package TestJavaCode.AuthApp.model;

import TestJavaCode.AuthApp.model.enam.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_one")
public class User {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_name")
    private String userName;
    private String password;
    @Column(name = "is_account_non_locked")
    private boolean isAccountNonLocked = true;
    @Column(name = "failed_attempts")
    private int failedAttempts;

    //    @ManyToMany
//    @JoinTable(
//        name = "users_roles",
//        joinColumns = @JoinColumn(name = "username"),
//        inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
    @Enumerated(EnumType.STRING)
    private Role role;

    public void incrementFailedAttempts() {
        this.failedAttempts++;
    }

    public void resetFailedAttempts() {
        this.failedAttempts = 0;
    }

}