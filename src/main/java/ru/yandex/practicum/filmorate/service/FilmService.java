package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;

@Service
public class FilmService {

    /**
     * Создайте FilmService, который будет отвечать за операции с фильмами, — добавление и удаление лайка,
     * вывод 10 наиболее популярных фильмов по количеству лайков. Пусть пока каждый пользователь может
     * поставить лайк фильму только один раз.
     */
    @Autowired
    private FilmStorage inMemoryFilmStorage;

    public long addLike(User user) {
        return 0;
    }

    public long removeLike (User user) {
        return 0;
    }

    public List<Film> topFilms() {
        return null;
    }

}
