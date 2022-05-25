package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

     Film addFilm(Film film);

     boolean removeFilm (long id);

     Film updateFilm (Film film);

     Film getFilm(long id);

     Collection<Film> getAllFilms();


}
