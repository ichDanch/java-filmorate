package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private HashMap<Long, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @NotNull @RequestBody User user) {
        validation(user);
        users.put(user.getId(), user);
        log.info("User added: " + user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @NotNull @RequestBody User user) {
        validation(user);
        users.put(user.getId(), user);
        log.info("User updated: " + user);
        return user;
    }

    @GetMapping
    public ArrayList<User> getAllUsers() {
        return new ArrayList<User>(users.values());
    }

    public void validation(User user) {
        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.trace("User name equals user login");
        }
    }
}

