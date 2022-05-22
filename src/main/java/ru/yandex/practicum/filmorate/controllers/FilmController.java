package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.film.FilmPredicate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    HashMap<Long, Film> films = new HashMap<>();

    @Autowired
    private List<FilmPredicate> filmValidators;

    @PostMapping
    public Film addFilm(@Valid @NotNull @RequestBody Film film) {
        validation(film);
        log.info("Film added: " + film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @NotNull @RequestBody Film film) {
        validation(film);
        log.info("Film updated: " + film);
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<Film>(films.values());
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
