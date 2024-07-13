package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film newFilm);

    void remove(long filmId);

    Collection<Film> findAll();

    Optional<Film> findById(long id);

    List<Film> findPopularFilm(Comparator<Film> comparator, long count);
}
