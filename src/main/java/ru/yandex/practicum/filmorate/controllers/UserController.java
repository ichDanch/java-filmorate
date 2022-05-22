package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;



    @PostMapping
    public User addUser(@Valid @NotNull @RequestBody User user) {
        validation(user);
        userService.addFriend(user.getId());
//        log.info("User added: " + user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @NotNull @RequestBody User user) {
        validation(user);
        log.info("User updated: " + user);
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
      //  return new ArrayList<User>(users.values());
    }

    public void validation(User user) {
        if (user.getId() == 0 ) {
            // throw new exception
        }

        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.trace("User name equals user login");
        }
    }
}

