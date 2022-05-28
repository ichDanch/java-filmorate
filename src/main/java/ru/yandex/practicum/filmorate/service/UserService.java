package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {


    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        validation(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        validation(user);
        userStorage.updateUser(user);
        return user;
    }

    public boolean removeUser(long id) {
        getUser(id);
        return userStorage.removeUser(id);
    }

    public User getUser(long id) {
        return userStorage.getUser(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Does not contain user with this id or id is negative " + id));
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(long userId, long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.setFriends(friendId);
        friend.setFriends(userId);
    }

    public void removeFriend(long userId, long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);

        if (!user.getFriends().contains(friendId) || !friend.getFriends().contains(userId)) {
            throw new UserNotFoundException("Users are not friends");
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getAllFriends(long id) {
        return getUser(id).getFriends().stream()
                .map(o -> {
                    return getUser(o);
                })
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {
        Set<Long> userFriends = getUser(id).getFriends();
        Set<Long> otherUserFriends = getUser(otherId).getFriends();

        if (userFriends == null || otherUserFriends == null) {
            return new ArrayList<>();
        }

        List<Long> commonFriends = new ArrayList<>(userFriends);
        commonFriends.retainAll(otherUserFriends);

        return commonFriends.stream()
                .map(o -> {
                    return getUser(o);
                })
                .collect(Collectors.toList());
    }


    public void validation(User user) {
        if (user.getId() < 0) {
            throw new UserNotFoundException("Negative or zero user id");
        }
        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
