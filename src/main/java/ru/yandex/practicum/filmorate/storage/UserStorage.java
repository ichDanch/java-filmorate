package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User addUser (User user);

    boolean removeUser (long id);

    User updateUser (User user);

    Collection<User> getAllUsers();

    Optional<User> getUser(long id);

}
