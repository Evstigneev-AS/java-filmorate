package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{FilmId}")
    public Film findFilm(@PathVariable("FilmId") long filmId) {
        return filmService.findById(filmId);
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/popular")
    public List<Film> findPopular(@RequestParam Optional<Long> count) {
        return filmService.findPopular(count);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    @DeleteMapping("/{FilmId}")
    public void remove(@PathVariable("FilmId") long userId) {
        filmService.remove(userId);
    }

    @PutMapping("/{FilmId}/like/{UserId}")
    public Film addLike(@PathVariable("FilmId") long filmId, @PathVariable("UserId") long userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{FilmId}/like/{UserId}")
    public Film removeLike(@PathVariable("FilmId") long filmId, @PathVariable("UserId") long userId) {
        return filmService.removeLike(filmId, userId);
    }
}