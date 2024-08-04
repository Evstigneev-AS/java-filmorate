package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        log.debug("Запрос на добавление нового фильма: {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException, NotFoundException {
        log.debug("Запрос на изменение фильма: {}", film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) throws NotFoundException {
        log.debug("Запрос на получение информации о фильме (ID: {})", id);
        return filmService.getFilm(id);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.debug("Запрос на получение списка фильмов");
        return filmService.getFilms();
    }

    @PutMapping("/{id}/like/{user_id}")
    public void addLike(@PathVariable Long id, @PathVariable(name = "user_id") Long userId) throws NotFoundException, ValidationException {
        log.debug("Запрос на установку лайка фильму: filmId={}, userId={}", id, userId);
        filmService.addUserLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{user_id}")
    public void deleteLike(@PathVariable Long id, @PathVariable(name = "user_id") Long userId) throws NotFoundException, ValidationException {
        log.debug("Запрос на снятие лайка фильму: filmId={}, userId={}", id, userId);
        filmService.deleteUserLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.debug("Запрос на получение списка популярных фильмов: count={}", count);
        return filmService.getPopular(count);
    }
}