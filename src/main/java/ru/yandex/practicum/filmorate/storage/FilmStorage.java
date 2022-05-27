package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

     Film addFilm(Film film);

     boolean removeFilm (long id);

     Film updateFilm (Film film);

     Optional<Film> getFilm(long id);

     Collection<Film> getAllFilms();


}
