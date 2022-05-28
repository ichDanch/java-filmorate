package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    HashMap<Long, Film> films = new HashMap<>();
    private long FILM_COUNT;

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
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }


}
