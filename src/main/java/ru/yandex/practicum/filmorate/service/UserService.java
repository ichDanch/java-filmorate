package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    /**
     * Создайте UserService, который будет отвечать за такие операции с пользователями, как добавление в друзья,
     * удаление из друзей, вывод списка общих друзей. Пока пользователям не надо одобрять заявки
     * в друзья — добавляем сразу. То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
     */

    @Autowired
    private UserStorage inMemoryUserStorage;

    public User addFriend (long id) {
        return null;
    }

    public boolean removeFriend (long id) {
        return false;
    }

    public List<User> commonFriends(){
        return null;
    }

    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }


}
