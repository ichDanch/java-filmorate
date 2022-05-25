package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean removeUser(long id) {
        idValidation(id);
        users.remove(id);
        return true;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(long id) {
        idValidation(id);
        return users.get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    public void idValidation(long id) {
        if (id <= 0) {
            throw new ValidationException("Negative or zero id " + id);
        }
        if (!users.containsKey(id)) {
            throw new ValidationException("Does not contain user with this id " + id);
        }
    }

}
