package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    /**
     * Создайте FilmService, который будет отвечать за операции с фильмами, — добавление и удаление лайка,
     * вывод 10 наиболее популярных фильмов по количеству лайков. Пусть пока каждый пользователь может
     * поставить лайк фильму только один раз.
     */

    private final FilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }


    public void addLike(long filmId, long userId) {
        if (filmId <= 0) {
            throw new ValidationException("Negative or zero film id when add like" + filmId);
        }
        if (userId <= 0) {
            throw new ValidationException("Negative or zero user id when add like" + userId);
        }
        Film film = inMemoryFilmStorage.getFilm(filmId);
        film.setLikesIdUsers(userId);
    }

    public void removeLike(long filmId, long userId) {
        if (filmId <= 0) {
            throw new ValidationException("Negative or zero film id when remove like" + filmId);
        }
        if (userId <= 0) {
            throw new ValidationException("Negative or zero user id when remove like " + userId);
        }
        Film film = inMemoryFilmStorage.getFilm(filmId);
        Set<Long> likes = film.getLikesIdUsers();
        if (!likes.contains(userId)) {
            throw new ValidationException("This user [" + userId + "] does not like this film )");
        }
        likes.remove(userId);
    }

    /**
     * GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10.
     */

    public List<Film> topFilms(int count) {
        if (count <= 0) {
            throw new ValidationException("Negative or zero count");
        }
        return inMemoryFilmStorage.getAllFilms()
                .stream()
                .sorted((f0, f1) -> {
                    return f0.getLikesIdUsers().size() - f1.getLikesIdUsers().size();
                })
                .skip(count)
                .collect(Collectors.toList());

    }

}
