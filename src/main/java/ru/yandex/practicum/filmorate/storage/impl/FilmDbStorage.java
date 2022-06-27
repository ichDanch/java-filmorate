package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private static final String FILM_INSERT =
            "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASEDATE, DURATION, MPA_ID)" +
                    " VALUES (?, ?, ?, ?, ?)";
    private static final String FILM_DELETE = "DELETE FROM FILMS WHERE film_id = ?";
    private static final String FILM_UPDATE =
            "UPDATE FILMS " +
                    "SET name = ?, description = ?, releaseDate = ?, duration = ?, MPA_ID = ? " +
                    "WHERE film_id = ?";
    private static final String FILM_SELECT = "SELECT * FROM FILMS WHERE film_id = ?";
    private static final String FILMS_SELECT_ALL = "SELECT * FROM films";
    private static final String FILMS_SELECT_TOP =
            "select FILMS.FILM_ID,NAME,DESCRIPTION,RELEASEDATE,DURATION, MPA_ID, count(LIKES.user_id) " +
                    "from FILMS " +
                    "left join likes on FILMS.FILM_ID = LIKES.FILM_ID " +
                    "group by films.FILM_ID " +
                    "order by count(LIKES.USER_ID) desc " +
                    "limit ?";
    private static final String GENRES_INSERT_TO_FILM = "insert into FILMS_GENRE (film_id, genre_id) values (?,?)";
    private static final String GENRES_DELETE_FROM_FILM = "DELETE FROM FILMS_GENRE WHERE FILM_ID=?";
    private static final String GENRES_SELECT_TO_FILM =
            "SELECT GENRES.GENRE_ID, NAME " +
                    "FROM FILMS_GENRE " +
                    "JOIN GENRES ON FILMS_GENRE.GENRE_ID = GENRES.GENRE_ID " +
                    "WHERE FILMS_GENRE.FILM_ID=?";

    private static final String LIKE_INSERT = "INSERT INTO LIKES (user_id, film_id) VALUES (?, ?)";
    private static final String LIKE_SELECT = "SELECT * FROM LIKES WHERE user_id = ? AND film_id = ?";
    private static final String LIKE_DELETE = "DELETE FROM LIKES where USER_ID=? AND FILM_ID=?";


    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage, MpaStorage mpaStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(FILM_INSERT, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
      /*  if (film.getMpa() != null) {
            int mpaId = film.getMpa().getId();
            Mpa mpa = mpaStorage.getMpa(mpaId).get();
            film.setMpa(mpa);
        }*/
        if (film.getGenres() != null) {
            addGenresToFilm(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public boolean removeFilm(long id) {
        return jdbcTemplate.update(FILM_DELETE, id) > 0;
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update(FILM_UPDATE,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (film.getGenres() != null) {
            removeGenresFromFilm(film.getId());
            addGenresToFilm(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public Optional<Film> getFilm(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(FILM_SELECT, id);
        if (userRows.next()) {
            Film film = new Film(
                    userRows.getLong("film_id"),
                    userRows.getString("name"),
                    userRows.getString("description"),
                    userRows.getDate("releaseDate").toLocalDate(),
                    userRows.getInt("duration")
            );
            int mpa = userRows.getInt("mpa_id");
            film.setMpa(mpaStorage.getMpa(mpa).get());

            Set<Genre> genres = getFilmGenres(id);
            if (genres.size() != 0) {
                film.setGenres(getFilmGenres(id));
            }
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> allFilms = new ArrayList<>();
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet(FILMS_SELECT_ALL);

        while (filmsRows.next()) {
            Film film = new Film(filmsRows.getInt("film_id"),
                    filmsRows.getString("name"),
                    filmsRows.getString("description"),
                    filmsRows.getDate("release_date").toLocalDate(),
                    filmsRows.getInt("duration"));
            film.setMpa(
                    mpaStorage.getMpa(
                            filmsRows.getInt("mpa_id")).get());
            film.setGenres(
                    getFilmGenres(film.getId())
            );

            allFilms.add(film);
        }
        return allFilms;
    }


    @Override
    public void addLike(long filmId, long userId) {
        jdbcTemplate.update(LIKE_INSERT, userId, filmId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(LIKE_SELECT, userId, filmId);
        if (userRows.next()) {
            jdbcTemplate.update(LIKE_DELETE, userId, filmId);
        } else {
            throw new GenreNotFoundException("Not like");
        }

    }

    @Override
    public List<Film> topFilms(int count) {
        List<Film> topFilms = new ArrayList<>();
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet(FILMS_SELECT_TOP, count);

        while (filmsRows.next()) {
            Film film = new Film(filmsRows.getInt("film_id"),
                    filmsRows.getString("name"),
                    filmsRows.getString("description"),
                    filmsRows.getDate("releaseDate").toLocalDate(),
                    filmsRows.getInt("duration"));
            film.setMpa(
                    mpaStorage.getMpa(
                            filmsRows.getInt("mpa_id")).get());
            film.setGenres(
                    getFilmGenres(film.getId())
            );

            topFilms.add(film);
        }
        return topFilms;
    }

    @Override
    public void addGenresToFilm(long filmId, TreeSet<Genre> genres) {
        for (Genre genre : genres) {
            jdbcTemplate.update(GENRES_INSERT_TO_FILM, filmId, genre.getId());
        }
    }

    @Override
    public void removeGenresFromFilm(long filmId) {
        jdbcTemplate.update(GENRES_DELETE_FROM_FILM, filmId);
    }

    @Override
    public TreeSet<Genre> getFilmGenres(Long filmId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(GENRES_SELECT_TO_FILM, filmId);

        TreeSet<Genre> genres = new TreeSet<>();

        while (sqlRowSet.next()) {
            genres.add(new Genre(sqlRowSet.getInt("genre_id"),
                    sqlRowSet.getString("name")));
        }
        return genres;
    }

}
