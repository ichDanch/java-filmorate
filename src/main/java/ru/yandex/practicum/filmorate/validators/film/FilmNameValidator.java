package ru.yandex.practicum.filmorate.validators.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.film.EmptyFilmNameException;
import ru.yandex.practicum.filmorate.model.Film;

@Service
public class FilmNameValidator implements FilmPredicate {
    @Override
    public boolean test(Film film) {
        return film.getName().isBlank();
    }

    @Override
    public RuntimeException getError() {
        return new EmptyFilmNameException("Film name is empty");
    }
}
