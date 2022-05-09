package ru.yandex.practicum.filmorate.exceptions.film;

public class FilmDescriptionIsTooLongException extends EmptyFilmNameException {
    public FilmDescriptionIsTooLongException() {
        super("Description must be less then 200 symbols");
    }
}
