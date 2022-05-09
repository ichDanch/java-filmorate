package ru.yandex.practicum.filmorate.exceptions.film;

public class FilmIdAlreadyExistException extends EmptyFilmNameException{
    public FilmIdAlreadyExistException() {
        super("Movie with this id is already exists");
    }
}
