package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private static final String USER_INSERT = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String USER_REMOVE = "DELETE FROM users WHERE user_id=?";
    private static final String USER_UPDATE =
            "UPDATE USERS " +
                    "SET email = ?, login = ?, name = ?, birthday = ? " +
                    "WHERE user_id = ?";
    private static final String USERS_GET_ALL = "SELECT * FROM USERS ";
    private static final String USER_GET = "SELECT * FROM USERS WHERE user_id = ?";
    private static final String FRIEND_INSERT = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)";
    private static final String FRIEND_DELETE = "DELETE FROM friends WHERE user_id=? AND friend_id=?";
    private static final String FRIENDS_SELECT_ALL =
            "select FRIEND_ID, EMAIL, LOGIN, NAME, BIRTHDAY" +
                    " from FRIENDS " +
                    "JOIn USERS ON FRIENDS.FRIEND_ID = USERS.USER_ID" +
                    " WHERE FRIENDS.USER_ID=?";

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(USER_INSERT, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public boolean removeUser(long id) {
        return jdbcTemplate.update(USER_REMOVE, id) > 0;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(USER_UPDATE,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        SqlRowSet usersRows = jdbcTemplate.queryForRowSet(USERS_GET_ALL);
        while (usersRows.next()) {
            allUsers.add(new User(
                    usersRows.getInt("user_id"),
                    usersRows.getString("email"),
                    usersRows.getString("login"),
                    usersRows.getString("name"),
                    usersRows.getDate("birthday").toLocalDate()));
        }
        return allUsers;
    }

    @Override
    public Optional<User> getUser(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(USER_GET, id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate()
            );
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void addFriend(long userId, long friendId) {
        jdbcTemplate.update(FRIEND_INSERT, userId, friendId, 1);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        jdbcTemplate.update(FRIEND_DELETE, userId, friendId);
    }

    @Override
    public List<User> getAllFriends(long id) {
        // работает
        return jdbcTemplate.query(FRIENDS_SELECT_ALL, (rs, rowNum) -> new User(
                        rs.getLong("friend_id"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getDate("birthday").toLocalDate()),
                id
        );

        // тоже работает

       /* List<Long> allFriendsId = new ArrayList<>();

        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("SELECT friend_id FROM friends WHERE status=1 AND user_id=?",id);
        while (friendsRows.next()) {
            allFriendsId.add(friendsRows.getLong("friend_id"));
        }
        return allFriendsId
                .stream()
                .map(this::getUser)
                .map(Optional::get)
                .collect(Collectors.toList());*/
    }
}
