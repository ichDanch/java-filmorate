package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Component
public class GenreDbStorage implements GenreStorage {
    private static final String GENRES_SELECT_ALL = "SELECT * FROM GENRES";
    private static final String GENRE_SELECT = "SELECT * FROM GENRES WHERE genre_id = ?";
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenre() {
        return jdbcTemplate.query(GENRES_SELECT_ALL, (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("name"))
        );
    }

    @Override
    public Optional<Genre> getGenre(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(GENRE_SELECT, id);
        if (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("genre_id"),
                    genreRows.getString("name"));

            return Optional.ofNullable(genre);
        } else {
            return Optional.empty();
        }
    }
}
