package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.film.FilmIdAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.film.InvalidFilmIdException;
import ru.yandex.practicum.filmorate.exceptions.user.EmptyUserEmailException;
import ru.yandex.practicum.filmorate.exceptions.user.EmptyUserLoginException;
import ru.yandex.practicum.filmorate.exceptions.user.UserBirthdayException;
import ru.yandex.practicum.filmorate.exceptions.user.UserIdAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    HashMap<Integer, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @NotNull @RequestBody User user)
            throws EmptyUserEmailException, EmptyUserLoginException {

        if (users.containsKey(user.getId())) {
            throw new UserIdAlreadyExistException();
        }
        if (user.getLogin().isBlank() || user.getLogin().isEmpty()) {
            throw new EmptyUserLoginException("Login is blank or misspelled");
        }
        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.trace("User name equals user login");
        }

       /* if (user.getEmail().isBlank() || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new EmptyUserEmailException("Email is blank or misspelled");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
        throw new UserBirthdayException("Incorrect birthday date");
        }*/
        users.put(user.getId(), user);
        log.trace("User added: " + user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @NotNull @RequestBody User user)
            throws EmptyUserEmailException {

        if (user.getEmail().isBlank() || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new EmptyUserEmailException("Email is blank or misspelled");
        }
        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
       /*
        if (user.getLogin().isBlank() || user.getLogin().isEmpty()) {
            throw new EmptyUserLoginException("Login is blank or misspelled");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserBirthdayException("Incorrect birthday date");
        }*/

        users.put(user.getId(), user);
        log.trace("User updated: " + user);
        return user;
    }

    @GetMapping
    public HashMap<Integer, User> getAllUsers() {

        return users;
    }
}

