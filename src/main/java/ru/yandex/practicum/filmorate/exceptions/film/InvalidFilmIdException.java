package ru.yandex.practicum.filmorate.exceptions.film;

public class InvalidFilmIdException extends EmptyFilmNameException{
    public InvalidFilmIdException() {
        super("Wrong id");
    }
}
