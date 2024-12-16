package com.JavaCode.OAuth2Auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class ControllerAuth {
    private static final Logger log = LoggerFactory.getLogger(ControllerAuth.class);

    @GetMapping(value = "login")
    public void getLogin(HttpServletResponse response) {

        try {
            response.sendRedirect("/oauth2/authorization/google");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
