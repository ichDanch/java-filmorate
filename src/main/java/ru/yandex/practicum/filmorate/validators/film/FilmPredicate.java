package ru.yandex.practicum.filmorate.validators.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.function.Predicate;

public interface FilmPredicate extends Predicate<Film> {
    RuntimeException getError();
}
