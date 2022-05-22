package ru.yandex.practicum.filmorate.exceptions;

public class ErrorResponse extends RuntimeException {
    private final String error;
    public ErrorResponse(String message) {
        super(message);
        this.error = message;
    }
}
