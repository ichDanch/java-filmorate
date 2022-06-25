package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public interface FilmStorage {

    Film addFilm(Film film);

    boolean removeFilm(long id);

    Film updateFilm(Film film);

    Optional<Film> getFilm(long id);

    List<Film> getAllFilms();

    List<Genre> getAllGenre();

    Optional<Genre> getGenre(int id);

    List<Mpa> getAllMpa();

    Optional<Mpa> getMpa(int id);

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);
    List<Film> topFilms(int count);
    void addGenresToFilm(long filmId, TreeSet<Genre> genres);
    void removeGenresFromFilm (long filmId);
    TreeSet<Genre> getFilmGenres(Long filmId);


}
