package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validators.film.FilmPredicate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Autowired
    private List<FilmPredicate> filmValidators;

    public Film addFilm(Film film) {
        validation(film);
        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        validation(film);
        getFilm(film.getId());
        filmStorage.updateFilm(film);
        return film;
    }

    public Film getFilm(long id) {
        return filmStorage.getFilm(id)
                .orElseThrow(() ->
                        new FilmNotFoundException("Does not contain a movie with this id or id is negative " + id));
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public boolean removeFilm(long id) {
        getFilm(id);
        return filmStorage.removeFilm(id);
    }

    public void addLike(long filmId, long userId) {
        getFilm(filmId);
        userStorage.getUser(userId);
        filmStorage.addLike(filmId,userId);
    }

    public void removeLike(long filmId, long userId) {
        getFilm(filmId);
        userStorage.getUser(userId);
        filmStorage.removeLike(filmId,userId);
    }

    public List<Film> topFilms(int count) {
      return filmStorage.topFilms(count);
       /* return filmStorage.getAllFilms()
                .stream()
                .sorted((f0, f1) -> {
                    return f1.getLikesIdUsers().size() - f0.getLikesIdUsers().size();
                })
                .limit(count)
                .collect(Collectors.toList());*/
    }

    public List<Genre> getAllGenre() {
        return filmStorage.getAllGenre();
    }

    public Genre getGenre(int id) {
        return filmStorage.getGenre(id)
                .orElseThrow(() ->
                        new GenreNotFoundException("Does not contain a genre with this id or invalid id " + id));
    }

    public List<Mpa> getAllMpa() {
        return filmStorage.getAllMpa();
    }

    public Mpa getMpa(int id) {
        return filmStorage.getMpa(id)
                .orElseThrow(() ->
                        new MpaNotFoundException("Does not contain a MPA with this id or invalid id " + id));
    }




    public void validation(Film film) {
        final var filmErrorValidator = filmValidators
                .stream()
                .filter(validator -> !validator.test(film))
                .findFirst();

        filmErrorValidator.ifPresent(validator -> {
            throw validator.getError();
        });
    }
}
