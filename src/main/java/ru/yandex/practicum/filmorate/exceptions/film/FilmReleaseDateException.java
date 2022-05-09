package ru.yandex.practicum.filmorate.exceptions.film;

public class FilmReleaseDateException extends EmptyFilmNameException {
    public FilmReleaseDateException() {
        super("Release date no earlier than December 28, 1895");
    }
}
