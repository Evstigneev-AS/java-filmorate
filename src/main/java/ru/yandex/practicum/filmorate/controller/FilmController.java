package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @GetMapping("/{filmId}")
    public Film findFilm(@PathVariable("filmId") long filmId) {
        return inMemoryFilmStorage.findById(filmId).orElseThrow(() ->
                new ConditionsNotMetException("Указанный фильм не найден"));
    }

    @GetMapping
    public Collection<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return inMemoryFilmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        return inMemoryFilmStorage.update(newFilm);
    }

    @DeleteMapping("/{filmId}")
    public void remove(@PathVariable("filmId") long userId) {
        inMemoryFilmStorage.remove(userId);
    }

    private void validateFilm(Film film) {
        inMemoryFilmStorage.validateFilm(film);
    }
}