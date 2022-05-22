package ru.yandex.practicum.filmorate.validators.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Service
public class FilmReleaseDateValidator implements FilmPredicate {
    @Override
    public boolean test(Film film) {
        if (film.getReleaseDate() != null) {
            return film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28));
        }
        return false;
    }

    @Override
    public RuntimeException getError() {
        return new ValidationException("Release date no earlier than December 28, 1895");
    }
}
