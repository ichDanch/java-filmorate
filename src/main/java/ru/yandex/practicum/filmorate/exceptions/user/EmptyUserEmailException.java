package ru.yandex.practicum.filmorate.exceptions.user;

public class EmptyUserEmailException extends Exception{
    public EmptyUserEmailException(String message) {
        super(message);
    }
}
