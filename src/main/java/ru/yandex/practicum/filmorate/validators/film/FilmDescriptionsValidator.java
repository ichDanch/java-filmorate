package ru.yandex.practicum.filmorate.validators.film;

import ru.yandex.practicum.filmorate.exceptions.film.FilmDescriptionIsTooLongException;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmDescriptionsValidator implements FilmPredicate {
    @Override
    public boolean test(Film film) {
        return film.getDescription().length() < 200;
    }

    @Override
    public RuntimeException getError() {
        return new FilmDescriptionIsTooLongException();
    }
}
