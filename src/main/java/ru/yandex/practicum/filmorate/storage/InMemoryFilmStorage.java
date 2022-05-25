package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    HashMap<Long, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        films.put(film.getId(), film);
        return null;
    }

    @Override
    public boolean removeFilm(long id) {
        idValidation(id);
        films.remove(id);
        return true;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilm(long id) {
       idValidation(id);
       return films.get(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    public void idValidation(long id) {
        if(id <= 0) {
            throw new ValidationException("Negative or zero id " + id);
        }
        if (!films.containsKey(id)) {
            throw new ValidationException("Does not contain a movie with this id " + id);
        }
    }
}
