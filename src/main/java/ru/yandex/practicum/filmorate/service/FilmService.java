package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validators.film.FilmPredicate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    /**
     * Создайте FilmService, который будет отвечать за операции с фильмами, — добавление и удаление лайка,
     * вывод 10 наиболее популярных фильмов по количеству лайков. Пусть пока каждый пользователь может
     * поставить лайк фильму только один раз.
     */

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
        idValidation(id);
        Film film = filmStorage.getFilm(id)
                .orElseThrow( ()->
                        new FilmNotFoundException("Does not contain a movie with this id or id is negative" + id));
        return film;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public boolean removeFilm(long id) {
        idValidation(id);
        return filmStorage.removeFilm(id);
    }

    public void addLike(long filmId, long userId) {
        if (filmId <= 0) {
            throw new FilmNotFoundException("Negative or zero film id when add like" + filmId);
        }
        if (userId <= 0) {
            throw new UserNotFoundException("Negative or zero user id when add like" + userId);
        }
        if (filmStorage.getFilm(filmId).isPresent()) {
            Film film = filmStorage.getFilm(filmId).get();
            film.setLikesIdUsers(userId);
        }

    }

    public void removeLike(long filmId, long userId) {
        if (filmId <= 0) {
            throw new FilmNotFoundException("Negative or zero film id when remove like" + filmId);
        }
        if (userId <= 0) {
            throw new UserNotFoundException("Negative or zero user id when remove like " + userId);
        }
        // эту часть как-то можно упростить, чтобы не использовать вложенный if
        if (filmStorage.getFilm(filmId).isPresent()) {
            Film film = filmStorage.getFilm(filmId).get();
            Set<Long> likes = film.getLikesIdUsers();
            if (!likes.contains(userId)) {
                throw new UserNotFoundException("This user [" + userId + "] does not like this film )");
            }
            likes.remove(userId);
        }
    }

    /**
     * GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10.
     */

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


    public void idValidation(long id) {
        var var = filmStorage.getAllFilms()
                .stream()
                .filter(film -> film.getId() > 0 && film.getId() == id)
                .findFirst()
                .orElseThrow(() ->
                        new FilmNotFoundException("Does not contain a movie with this id or id is negative" + id));
    }

}
