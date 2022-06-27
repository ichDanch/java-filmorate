package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Long, User> users = new HashMap<>();
    private long USER_COUNT;

    @Override
    public User addUser(User user) {
        user.setId(++USER_COUNT);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean removeUser(long id) {
        users.remove(id);
        return true;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getUser(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void addFriend(long userId, long friendId) {

    }

    @Override
    public void removeFriend(long userId, long friendId) {

    }

    @Override
    public List<User> getAllFriends(long id) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}


