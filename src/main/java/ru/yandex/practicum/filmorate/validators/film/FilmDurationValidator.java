package ru.yandex.practicum.filmorate.validators.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@Service
public class FilmDurationValidator implements FilmPredicate {
    @Override
    public boolean test(Film film) {
        if (film.getDuration() <= 0 ) {
            return film.getDuration() > 0;
        }
        return false;
    }

    @Override
    public RuntimeException getError() {
        return new ValidationException("Film duration can't be less then zero");

    }
}
