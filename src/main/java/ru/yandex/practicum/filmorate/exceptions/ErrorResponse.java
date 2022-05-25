package ru.yandex.practicum.filmorate.exceptions;

public class ErrorResponse{
    private final String error;

    public ErrorResponse(String message) {
        this.error = message;
    }

    public String getError() {
        return error;
    }
}
