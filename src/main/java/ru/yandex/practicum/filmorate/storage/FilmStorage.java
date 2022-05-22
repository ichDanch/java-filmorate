package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

     Film addFilm(Film film);

     boolean removeFilm (Film film);

     Film updateFilm (Film film);
}
