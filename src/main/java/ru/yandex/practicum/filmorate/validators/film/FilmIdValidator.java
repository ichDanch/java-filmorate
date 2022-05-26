package ru.yandex.practicum.filmorate.validators.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@Service
public class FilmIdValidator implements FilmPredicate {
    @Override
    public boolean test(Film film) {
        return film.getId() >= 0;
    }

    @Override
    public RuntimeException getError() {
        return new FilmNotFoundException("Film id is negative");

    }
}
