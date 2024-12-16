package com.javaCode.SSTaskOne.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class UserController {


    @GetMapping("/home")
    public String home() {
        return "Добро пожаловать! Вы успешно аутентифицированы.";
    }

    @GetMapping("/hello")
    public String users() {
        return "Привет";
    }
}
