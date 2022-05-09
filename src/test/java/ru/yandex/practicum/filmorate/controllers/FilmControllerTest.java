package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.SpringApplication;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.exceptions.film.FilmDurationException;
import ru.yandex.practicum.filmorate.exceptions.film.FilmIdAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.film.FilmReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.constraints.Positive;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void createFilmController() {
        filmController = new FilmController();
    }

    @Test
    public void shouldAddFilm() {

        Film film = new Film(
                1,
                "film",
                "description",
                LocalDate.of(2020, 12, 12),
                Duration.ofMinutes(113));

        filmController.addFilm(film);
        Assertions.assertEquals(film, filmController.getAllFilms().get(1));
    }

    @Test
    public void shouldNotAddFilmWithSameId() {

        Film film1 = new Film(
                1,
                "film1",
                "description1",
                LocalDate.of(2020, 12, 12),
                Duration.ofMinutes(113));

        Film film2 = new Film(
                1,
                "film2",
                "description2",
                LocalDate.of(2020, 12, 12),
                Duration.ofMinutes(113));

        filmController.addFilm(film1);
        Assertions.assertEquals(film1, filmController.getAllFilms().get(1));

        final FilmIdAlreadyExistException exception = assertThrows(
                // класс ошибки
                FilmIdAlreadyExistException.class,
                // создание и переопределение экземпляра класса Executable
                new Executable() {
                    @Override
                    public void execute() {
                        // здесь блок кода, который хотим проверить
                        filmController.addFilm(film2);
                        ;
                    }
                });

        // можно проверить, находится ли в exception ожидаемый текст
        System.out.println(exception.getMessage());
        assertEquals("Movie with this id is already exists", exception.getMessage());
    }

    @Test
    public void shouldNotAddFilmWithDateReleaseBelow1895() {

        Film film1 = new Film(
                1,
                "film1",
                "description1",
                LocalDate.of(1894, 12, 12),
                Duration.ofMinutes(113));

        final FilmReleaseDateException exception = assertThrows(
                // класс ошибки
                FilmReleaseDateException.class,
                // создание и переопределение экземпляра класса Executable
                new Executable() {
                    @Override
                    public void execute() {
                        // здесь блок кода, который хотим проверить
                        filmController.addFilm(film1);
                        ;
                    }
                });

        // можно проверить, находится ли в exception ожидаемый текст
        System.out.println(exception.getMessage());
        assertEquals("Release date no earlier than December 28, 1895", exception.getMessage());
    }

    @Test
    public void shouldNotAddFilmWithNegativeDuration() {

        Film film1 = new Film(
                1,
                "film1",
                "description1",
                LocalDate.of(1901, 12, 12),
                Duration.ofMinutes(-113));

        final FilmDurationException exception = assertThrows(
                FilmDurationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        filmController.addFilm(film1);
                        ;
                    }
                });

        System.out.println(exception.getMessage());
        assertEquals("Film duration can't be zero or less then zero", exception.getMessage());
    }


}
