package ru.yandex.practicum.filmorate.exceptions.film;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyFilmNameException extends RuntimeException{
    public EmptyFilmNameException(String message) {
        super(message);
    }
}
