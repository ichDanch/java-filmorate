package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film addFilm(Film film);

    boolean removeFilm(long id);

    Film updateFilm(Film film);

    Optional<Film> getFilm(long id);

    List<Film> getAllFilms();


}
