package ru.yandex.practicum.filmorate.exceptions.film;

public class FilmDurationException extends EmptyFilmNameException{
    public FilmDurationException() {
        super("Film duration can't be zero or less then zero");
    }
}
