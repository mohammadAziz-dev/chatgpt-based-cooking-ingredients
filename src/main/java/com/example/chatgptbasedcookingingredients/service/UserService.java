package com.example.chatgptbasedcookingingredients.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email, String password) {
        AtomicBoolean created = new AtomicBoolean(false);

        User user = usersByEmail.computeIfAbsent(email, e -> {
            created.set(true);
            return new User(e, passwordEncoder.encode(password));
        });

        if (!created.get()) {
            throw new EmailAlreadyRegisteredException(email);
        }

        return user;
    }
}
