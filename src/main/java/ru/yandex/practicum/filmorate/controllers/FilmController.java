package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validators.film.FilmPredicate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {


    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @Autowired
    private List<FilmPredicate> filmValidators;

    @PostMapping
    public Film addFilm(@Valid @NotNull @RequestBody Film film) {
        validation(film);
        filmStorage.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @NotNull @RequestBody Film film) {
        validation(film);
        filmStorage.updateFilm(film);
        return film;
    }

    @GetMapping
    public Film getFilm(long id) {
        return filmStorage.getFilm(id);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @DeleteMapping
    public boolean removeFilm(long id) {
        return filmStorage.removeFilm(id);
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

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable(name = "id") long filmId,
                        @PathVariable long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") long filmId,
                           @PathVariable long userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> topFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) int count) {
       return filmService.topFilms(count);
    }


}
