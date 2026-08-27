package com.example.chatgptbasedcookingingredients;

import com.example.chatgptbasedcookingingredients.service.EmailAlreadyRegisteredException;
import com.example.chatgptbasedcookingingredients.service.User;
import com.example.chatgptbasedcookingingredients.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    ResponseEntity<String> register(@RequestBody(required = false) RegisterRequest request) {
        if (request == null || request.email() == null || request.email().isBlank()) {
            return ResponseEntity.badRequest().body("Email must not be blank");
        }
        if (request.password() == null || request.password().isBlank()) {
            return ResponseEntity.badRequest().body("Password must not be blank");
        }

        try {
            User user = userService.register(request.email(), request.password());
            return ResponseEntity.status(HttpStatus.CREATED).body(user.email());
        } catch (EmailAlreadyRegisteredException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
