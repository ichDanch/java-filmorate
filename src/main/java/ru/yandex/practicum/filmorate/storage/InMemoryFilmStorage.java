package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    HashMap<Long, Film> films = new HashMap<>();
    private static long FILM_COUNT;

    @Override
    public Film addFilm(Film film) {
        film.setId(++FILM_COUNT);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean removeFilm(long id) {
        films.remove(id);
        return true;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> getFilm(long id) {
       return Optional.of(films.get(id));
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }


}
