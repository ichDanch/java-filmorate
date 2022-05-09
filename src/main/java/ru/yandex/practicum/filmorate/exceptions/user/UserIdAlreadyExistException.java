package ru.yandex.practicum.filmorate.exceptions.user;

import ru.yandex.practicum.filmorate.exceptions.film.EmptyFilmNameException;

public class UserIdAlreadyExistException extends EmptyUserEmailException {
    public UserIdAlreadyExistException() {
        super("User with this id is already exists");
    }
}
