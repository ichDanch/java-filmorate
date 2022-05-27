package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@Service
public class UserService {
    /**
     * Создайте UserService, который будет отвечать за такие операции с пользователями, как добавление в друзья,
     * удаление из друзей, вывод списка общих друзей. Пока пользователям не надо одобрять заявки
     * в друзья — добавляем сразу. То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
     */

    @Autowired
    private UserStorage userStorage;

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
        idValidation(id);
        return userStorage.removeUser(id);
    }

    public User getUser(long id) {
        idValidation(id);
        User user = userStorage.getUser(id).orElseThrow(() ->
                new UserNotFoundException("Does not contain user with this id or id is negative " + id));
        return user;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }


    public void addFriend(long userId, long friendId) {
        if (userId <= 0) {
            throw new UserNotFoundException("Negative or zero user id when add friend, user id = " + userId);
        }
        if (friendId <= 0) {
            throw new UserNotFoundException("Negative or zero friend id when add friend, friend id = " + friendId);
        }
        if (userStorage.getUser(userId).isPresent() && userStorage.getUser(friendId).isPresent()) {
            User user = userStorage.getUser(userId).get();
            User friend = userStorage.getUser(friendId).get();
            user.setFriends(friendId);
            friend.setFriends(userId);
        }

    }

    public void removeFriend(long userId, long friendId) {
        if (userId <= 0) {
            throw new UserNotFoundException("Negative or zero user id when remove friend, user id = " + userId);
        }
        if (friendId <= 0) {
            throw new UserNotFoundException("Negative or zero friend id when remove friend, friend id = " + friendId);
        }

        if (userStorage.getUser(userId).isEmpty() && userStorage.getUser(friendId).isEmpty()) {
            throw new UserNotFoundException("User or friend not found");
        }
        User user = userStorage.getUser(userId).get();
        User friend = userStorage.getUser(friendId).get();

        if (!user.getFriends().contains(friendId) || !friend.getFriends().contains(userId)) {
            throw new UserNotFoundException("Users does not friends");
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getAllFriends(long id) {
        if (id <= 0) {
            throw new UserNotFoundException("Negative or zero id in get all friends: " + id);
        }
        if (userStorage.getUser(id).isEmpty()) {
            throw new UserNotFoundException("User not found: " + id);
        }
        User user = userStorage.getUser(id).get();
        Set<Long> friends = user.getFriends();
        List<User> friendsList = new ArrayList<>();
        for (Long friend : friends) {
            if (userStorage.getUser(friend).isPresent()) {
                User fr = userStorage.getUser(friend).get();
                friendsList.add(fr);
            }
        }
        return friendsList;
    }

    public List<User> getCommonFriends(long id, long otherId) {
        if (id <= 0) {
            throw new UserNotFoundException("Negative or zero id in get common friends: " + id);
        }
        if (otherId <= 0) {
            throw new UserNotFoundException("Negative or zero other id in get common friends: " + id);
        }
        if (userStorage.getUser(id).isEmpty() && userStorage.getUser(otherId).isEmpty()) {
            throw new UserNotFoundException("User or friend not found " + id);
        }

        Set<Long> userFriends = userStorage.getUser(id).get().getFriends();
        Set<Long> otherUserFriends = userStorage.getUser(otherId).get().getFriends();
        if (userFriends == null || otherUserFriends == null) {
            return new ArrayList<>();
        }
        List<Long> commonFriends = new ArrayList<>(userFriends);
        var var = commonFriends.retainAll(otherUserFriends);
        List<User> getCommonFriends = new ArrayList<>();
        for (Long friend : commonFriends) {
            if (userStorage.getUser(friend).isPresent()) {
                User user = userStorage.getUser(friend).get();
                getCommonFriends.add(user);
            }
        }
        return getCommonFriends;
    }

    public void idValidation(long id) {
        var var = userStorage.getAllUsers()
                .stream()
                .filter(user -> user.getId() > 0 && user.getId() == id)
                .findFirst()
                .orElseThrow(() ->
                        new UserNotFoundException("Does not contain user with this id or id is negative " + id));
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
