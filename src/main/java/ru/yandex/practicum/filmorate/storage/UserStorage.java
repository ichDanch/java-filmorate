package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    User addUser (User user);

    boolean removeUser (User user);

    User updateUser (User user);

    Collection<User> getAllUsers();

}
