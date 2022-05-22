package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage{
    private HashMap<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        users.put(user.getId(), user);
        return null;
    }

    @Override
    public boolean removeUser(User user) {
        return false;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return null;
    }

    @Override
    public Collection<User> getAllUsers() {
        var var = users.values();
        return var;
    }
}
