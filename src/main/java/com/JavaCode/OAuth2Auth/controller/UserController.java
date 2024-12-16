package com.JavaCode.OAuth2Auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping
    public ResponseEntity<?> pageHome() {

        return ResponseEntity.ok()
            .body("Домашняя страница" + "User Authorities: " + SecurityContextHolder.getContext().getAuthentication());
    }

    @GetMapping(value = "h2-console")
    public ResponseEntity<?> getConsole() {
        return ResponseEntity.ok().body("Консоль");
    }


    @GetMapping(value = "admin")
    public ResponseEntity<?> getAdmin() {
        return ResponseEntity.ok().body("все получилось");
    }

    @GetMapping(value = "home")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok().body("Домашняя страница");
    }

    @GetMapping(value = "user")

    public String user(@AuthenticationPrincipal OAuth2User principal, Model model) {
        model.addAttribute("name", principal.getAttribute("given_name"));
        model.addAttribute("surname", principal.getAttribute("family_name"));
        model.addAttribute("login", principal.getAttribute("email"));
        model.addAttribute("id", principal.getAttribute("sub"));
        model.addAttribute("email", principal.getAttribute("email"));
        System.out.println("Attributes: " + principal.getAttributes());
        return "user";
    }

    @GetMapping(value = "s_admin")
    public ResponseEntity<?> s_admin() {
        return ResponseEntity.ok().body("Домашняя страница");
    }

}
