package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

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

    public void addFriend(long userId, long friendId) {
        if (userId <= 0) {
            throw new UserNotFoundException("Negative or zero user id when add friend, user id = " + userId);
        }
        if (friendId <= 0) {
            throw new UserNotFoundException("Negative or zero friend id when add friend, friend id = " + friendId);
        }
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.setFriends(friendId);
        friend.setFriends(userId);
    }

    public void removeFriend(long userId, long friendId) {
        if (userId <= 0) {
            throw new UserNotFoundException("Negative or zero user id when remove friend, user id = " + userId);
        }
        if (friendId <= 0) {
            throw new UserNotFoundException("Negative or zero friend id when remove friend, friend id = " + friendId);
        }
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

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
        User user = userStorage.getUser(id);
        Set<Long> friends = user.getFriends();
        List<User> friendsList = new ArrayList<>();
        for (Long friend : friends) {
            User fr = userStorage.getUser(friend);
            friendsList.add(fr);
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
        Set<Long> userFriends = userStorage.getUser(id).getFriends();
        Set<Long> otherUserFriends = userStorage.getUser(otherId).getFriends();
        if (userFriends == null || otherUserFriends == null) {
            return new ArrayList<>();
        }
        List<Long> commonFriends = new ArrayList<>(userFriends);
        var var = commonFriends.retainAll(otherUserFriends);
        List<User> getCommonFriends = new ArrayList<>();
        for (Long friend : commonFriends) {
            User user = userStorage.getUser(friend);
            getCommonFriends.add(user);
        }
        return getCommonFriends;
    }


}
