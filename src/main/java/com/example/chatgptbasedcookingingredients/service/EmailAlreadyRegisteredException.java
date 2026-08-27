package com.example.chatgptbasedcookingingredients.service;

public class EmailAlreadyRegisteredException extends RuntimeException {

    public EmailAlreadyRegisteredException(String email) {
        super("Email already registered: " + email);
    }
}
