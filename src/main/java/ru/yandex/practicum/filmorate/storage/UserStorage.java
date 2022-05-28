package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User addUser(User user);

    boolean removeUser(long id);

    User updateUser(User user);

    List<User> getAllUsers();

    Optional<User> getUser(long id);

}
