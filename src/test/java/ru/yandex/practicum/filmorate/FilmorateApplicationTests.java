package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmController filmController;

    @Autowired
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllFilms() throws Exception {
        this.mockMvc.perform(get("/films"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAddValidFilm() throws Exception {
        Film film = new Film(
                1,
                "film",
                "description",
                LocalDate.of(2020, 12, 12),
                Duration.ofMinutes(113));

        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("film"))
                .andExpect(jsonPath("$.releaseDate").value("2020-12-12"))
                .andExpect(jsonPath("$.duration").value("PT1H53M"));
    }

    @Test
    public void shouldNotAddFilmWithEmptyName() throws Exception {
        Film film = new Film(
                1,
                "",
                "description",
                LocalDate.of(2020, 12, 12),
                Duration.ofMinutes(113));

        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAddFilmWithDescriptionIsLongerThan200() throws Exception {
        Film film = new Film(
                1,
                "film",
                "Йеллоустоун – первый в мире национальный парк, одно из самых посещаемых мест в США. " +
                        "Но здесь, на границе цивилизации, происходит много такого, чего не видят туристы, " +
                        "что не освещается средствами массов",
                LocalDate.of(2020, 12, 12),
                Duration.ofMinutes(113));

        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAddFilmWithReleaseDateBeforeYear1895Month12Day28() throws Exception {
        Film film = new Film(
                1,
                "film",
                "Description",
                LocalDate.of(1895, 12, 27),
                Duration.ofMinutes(113));

        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAddFilmWithNegativeDuration() throws Exception {
        Film film = new Film(
                1,
                "film",
                "Description",
                LocalDate.of(1928, 12, 27),
                Duration.ofMinutes(-1));

        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldPutValidFilm() throws Exception {
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
                LocalDate.of(2019, 3, 7),
                Duration.ofMinutes(157));

        filmController.addFilm(film1);

        mockMvc.perform(
                        put("/films")
                                .content(objectMapper.writeValueAsString(film2))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.description").value("description2"))
                .andExpect(jsonPath("$.name").value("film2"))
                .andExpect(jsonPath("$.releaseDate").value("2019-03-07"))
                .andExpect(jsonPath("$.duration").value("PT2H37M"));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAddValidUser() throws Exception {
        User user = new User(
                1,
                "mail@mail",
                "login",
                "name",
                LocalDate.of(1980, 12, 12));

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("mail@mail"))
                .andExpect(jsonPath("$.login").value("login"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.birthday").value("1980-12-12"));
    }

    @Test
    public void shouldNotAddUserWithoutEmail() throws Exception {
        User user = new User(
                1,
                null,
                "login",
                "name",
                LocalDate.of(1980, 12, 12));

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAddUserWithIncorrectEmail() throws Exception {
        User user = new User(
                1,
                "email",
                "login",
                "name",
                LocalDate.of(1980, 12, 12));

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAddUserWithEmptyLogin() throws Exception {
        User user = new User(
                1,
                "mail@mail",
                null,
                "name",
                LocalDate.of(1980, 12, 12));

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAddUserWithBlankInLogin() throws Exception {
        User user = new User(
                1,
                "mail@mail",
                "login login",
                "name",
                LocalDate.of(1980, 12, 12));

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAddUserWithFutureBirthday() throws Exception {
        User user = new User(
                1,
                "mail@mail",
                "login",
                "name",
                LocalDate.of(2323, 1, 1));

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldAddUserWithEmptyNameEqualsLogin() throws Exception {
        User user = new User(
                1,
                "mail@mail",
                "login",
                "",
                LocalDate.of(1980, 12, 12));

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("login"));
    }

    @Test
    public void shouldPutValidUser() throws Exception {
        User user1 = new User(
                1,
                "mail@mail1",
                "login1",
                "name1",
                LocalDate.of(1980, 12, 12));

        User user2 = new User(
                1,
                "mail@mail1",
                "login2",
                "name2",
                LocalDate.of(1990, 12, 13));

        userController.addUser(user1);

        mockMvc.perform(
                        put("/users")
                                .content(objectMapper.writeValueAsString(user2))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("mail@mail1"))
                .andExpect(jsonPath("$.login").value("login2"))
                .andExpect(jsonPath("$.name").value("name2"))
                .andExpect(jsonPath("$.birthday").value("1990-12-13"));
    }


}
