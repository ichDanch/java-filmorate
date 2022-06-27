package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    public void testAddUserAndGetUser() {
        userStorage.addUser(new User(
                1,
                "mail@mail",
                "login",
                "name",
                LocalDate.parse("1976-08-20")));

        Optional<User> userOptional = userStorage.getUser(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testUpdateUserAndGetUser() {
        userStorage.addUser(new User(
                1,
                "mail@mail",
                "login",
                "name",
                LocalDate.parse("1976-08-20")));

        userStorage.updateUser(new User(
                1,
                "mailmail@mail",
                "new login",
                "new name",
                LocalDate.parse("1976-09-20")));

        Optional<User> userOptional = userStorage.getUser(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "new name")
                );
    }
    @Test
    public void testAddFilmAndGetFilm() {
        Film film = new Film( 1,
                "film",
                "new description",
                LocalDate.parse("1999-04-30"),
                120);
        film.setMpa(new Mpa(1,"G"));

        filmDbStorage.addFilm(film);

        Optional<Film> filmOptional = filmDbStorage.getFilm(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("id", 1L)
                );
    }
    @Test
    public void testUpdateFilmAndGetFilm() {
        Film film1 = new Film( 1,
                "film",
                "description",
                LocalDate.parse("1999-04-30"),
                120);
        film1.setMpa(new Mpa(1,"G"));

        filmDbStorage.addFilm(film1);

        Film film2 = new Film( 1,

                "new film",
                "description",
                LocalDate.parse("1999-04-30"),
                120);
        film2.setMpa(new Mpa(1,"G"));

        filmDbStorage.updateFilm(film2);

        Optional<Film> filmOptional = filmDbStorage.getFilm(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("name", "new film")
                );
    }


    }

