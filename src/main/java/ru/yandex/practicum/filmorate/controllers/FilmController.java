package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.validators.film.FilmPredicate;
import ru.yandex.practicum.filmorate.exceptions.film.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    HashMap<Integer, Film> films = new HashMap<>();

    /*@Autowired
    private List<FilmPredicate> filmValidators;*/

    @PostMapping
    public Film addFilm(@Valid @NotNull @RequestBody Film film) {

       /* final var filmErrorValidator = filmValidators.stream()
                .filter(validator -> !validator.test(film))
                .findFirst();

        filmErrorValidator.ifPresent(validator -> {
            throw validator.getError();
        });*/

        if (films.containsKey(film.getId())) {
            throw new FilmIdAlreadyExistException();
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmReleaseDateException();
        }
        if (film.getDuration().toMinutes() <= 0) {
            throw new FilmDurationException();
        }

        /*if (film.getId() <= 0) {
            throw new InvalidFilmIdException();
        }
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new EmptyFilmNameException("Film name is empty");
        }
        if (film.getDescription().length() > 200) {
            throw new FilmDescriptionIsTooLongException();
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmReleaseDateException();
        }
      */

        log.info("Film added: " + film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @NotNull @RequestBody Film film) throws EmptyFilmNameException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmReleaseDateException();
        }
        if (film.getDuration().toMinutes() <= 0) {
            throw new FilmDurationException();
        }

        /*if (film.getId() <= 0 || !films.containsKey(film.getId())) {
            throw new InvalidFilmIdException();
        }*/

        /*if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new EmptyFilmNameException("Film name is empty");
        }
        if (film.getDescription().length() > 200) {
            throw new FilmDescriptionIsTooLongException();
        } */

        log.info("Film updated: " + film);
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public HashMap<Integer, Film> getAllFilms() {
        /*films.put(1,new Film(
                1,
                "film",
                "description",
                LocalDate.of(2020,12,12),
                Duration.ofMinutes(113)));

        films.put(2,new Film(
                2,
                "film",
                "description",
                LocalDate.of(2020,11,11),
                Duration.ofMinutes(115)));*/
        return films;
    }
}
