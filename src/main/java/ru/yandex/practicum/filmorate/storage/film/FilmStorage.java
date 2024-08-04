package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmStorage {

    List<Film> getFilmsList();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilm(Long id);

    void addUserLike(Long filmId, Long userId);

    void deleteUserLike(Long filmId, Long userId);

    List<Film> getPopular(Integer count);

    Set<Long> getLikes(Long filmId);

}
