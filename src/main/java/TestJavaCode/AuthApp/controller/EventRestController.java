package TestJavaCode.AuthApp.controller;

import TestJavaCode.AuthApp.dto.requestDTO.UnlockRequestDTO;
import TestJavaCode.AuthApp.exception.CustomExceptionNotFound;
import TestJavaCode.AuthApp.model.User;
import TestJavaCode.AuthApp.model.enam.Role;
import TestJavaCode.AuthApp.repository.IUserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController

@RequestMapping(value = "/api/v1/app")
public class EventRestController {

    private final IUserRepository iUserRepository;


    public EventRestController(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @GetMapping(value = "/home")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getPageHome() {
        return ResponseEntity.ok("Доступ ддля аутентифицированного пользователя!");
    }

    @GetMapping(value = "/moderation/content")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> getModeration() {
        return ResponseEntity.ok("Page work!");
    }

    @PreAuthorize("hasAnyAuthority('USER','SUPER_ADMIN')")
    @GetMapping(value = "/profile")

    public ResponseEntity<?> getRefreshAccessToken() {
        return ResponseEntity.ok("доступ  к  данным пользователя");
    }

    @GetMapping(value = "/access")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> getAllRole() {
        List<User> list = iUserRepository.getAllByUser()
            .orElseThrow(() -> new CustomExceptionNotFound("Роли не найдены"));

        Map<String, Role> listMap = list.stream().collect(Collectors.toMap(User::getUserName, User::getRole));
        return ResponseEntity.ok(listMap);

    }

    @PatchMapping(value = "/unlocking")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> unlockingUsers(@RequestBody @Valid UnlockRequestDTO userName) {

        Boolean statusBlocked = iUserRepository.getBlockingStatus(userName.getUserName())
            .orElseThrow(() -> new CustomExceptionNotFound("Пользователь не найден"));
        if (!statusBlocked) {
            iUserRepository.blockingStatusUpdate(true, 0, userName.getUserName());
            return ResponseEntity.ok("Пользователь разблокирован");
        }
        return ResponseEntity.ok("Пользователь  не заблокирован");
    }

    @PostMapping(value = "/access/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> addRoleUsers() {
        return ResponseEntity.ok("Роль назначена");
    }

}
