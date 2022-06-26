package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Component
public class MpaDbStorage implements MpaStorage {
    private static final String MPA_SELECT_ALL = "SELECT * FROM mpa_rating";
    private static final String MPA_SELECT = "SELECT * FROM MPA_RATING WHERE mpa_id=?";
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query(MPA_SELECT_ALL, (rs, rowNum) -> new Mpa(
                rs.getInt("mpa_id"),
                rs.getString("name"))
        );
    }

    @Override
    public Optional<Mpa> getMpa(int id) {
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(MPA_SELECT, id);
        if (mpaRow.next()) {
            Mpa mpa = new Mpa(
                    mpaRow.getInt("mpa_id"),
                    mpaRow.getString("name"));
            return Optional.of(mpa);
        } else {
            return Optional.empty();
        }
    }
}
