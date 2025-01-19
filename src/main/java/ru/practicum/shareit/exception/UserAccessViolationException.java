package ru.practicum.shareit.exception;

public class UserAccessViolationException extends RuntimeException {

    public UserAccessViolationException(String message) {
        super(message);
    }
}