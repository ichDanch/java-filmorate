package ru.yandex.practicum.filmorate.validators.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@Service
public class FilmDurationValidator implements FilmPredicate {
    @Override
    public boolean test(Film film) {
        return film.getDuration() >= 0;
    }

    @Override
    public RuntimeException getError() {
        return new ValidationException("Film duration can't be less then zero");

    }
}
