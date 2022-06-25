package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {


    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        validation(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        validation(user);
        getUser(user.getId());
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
        getUser(userId);
        getUser(friendId);
        userStorage.addFriend(userId,friendId);
    }

    public void removeFriend(long userId, long friendId) {
        getUser(userId);
        getUser(friendId);
        //проверка на друзей
        userStorage.removeFriend(userId,friendId);
    }

    public List<User> getAllFriends(long id) {
        return userStorage.getAllFriends(id);
    }

    public List<User> getCommonFriends(long id, long otherId) {

        List<User> userFriends = getAllFriends(id);
        List<User> otherUserFriends = getAllFriends(otherId);

        if (userFriends == null || otherUserFriends == null) {
            return new ArrayList<>();
        }

        List<User> commonFriends = new ArrayList<>(userFriends);
        commonFriends.retainAll(otherUserFriends);

        return commonFriends;
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
