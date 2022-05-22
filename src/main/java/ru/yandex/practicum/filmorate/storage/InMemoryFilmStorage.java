package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class InMemoryFilmStorage implements FilmStorage{


    @Override
    public Film addFilm(Film film) {
        return null;
    }

    @Override
    public boolean removeFilm(Film film) {
        return false;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }
}
