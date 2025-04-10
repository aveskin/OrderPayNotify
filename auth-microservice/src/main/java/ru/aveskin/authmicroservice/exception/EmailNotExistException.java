package ru.aveskin.authmicroservice.exception;

public class EmailNotExistException extends RuntimeException {
    public EmailNotExistException(String email) {
        super(email + " email не существует");
    }
}