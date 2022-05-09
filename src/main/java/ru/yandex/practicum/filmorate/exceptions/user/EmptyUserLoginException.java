package ru.yandex.practicum.filmorate.exceptions.user;

public class EmptyUserLoginException extends Exception{
    public EmptyUserLoginException(String message) {
        super(message);
    }
}
