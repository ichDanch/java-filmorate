package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validators.film.FilmPredicate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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
        Film film = getFilm(filmId);
        film.setLikesIdUsers(userId);
    }

    public void removeLike(long filmId, long userId) {
        // Как написать код для удаления элемента через stream ?
        Film film = getFilm(filmId);
        Set<Long> likes = film.getLikesIdUsers();
        if (!likes.contains(userId)) {
            throw new UserNotFoundException("This user [" + userId + "] does not liked this film )");
        }
        likes.remove(userId);
    }

    public List<Film> topFilms(int count) {
        if (count <= 0) {
            throw new FilmNotFoundException("Negative or zero count");
        }
        return filmStorage.getAllFilms()
                .stream()
                .sorted((f0, f1) -> {
                    return f1.getLikesIdUsers().size() - f0.getLikesIdUsers().size();
                })
                .limit(count)
                .collect(Collectors.toList());
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
