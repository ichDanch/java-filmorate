package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Film {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;
    private TreeSet<Long> likesIdUsers = new TreeSet<>();
    private TreeSet<Genre> genres;

    public Film(long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void setLikesIdUsers(long idUser) {
        likesIdUsers.add(idUser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;

        Film film = (Film) o;

        return getId() == film.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}
