package ru.aveskin.authmicroservice.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super(username + " не найден");
    }
}