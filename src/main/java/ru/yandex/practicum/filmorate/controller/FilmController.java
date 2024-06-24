package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Creating film with name: {}", film.getName());
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Film created: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Updating film with id: {}", newFilm.getId());
        if (newFilm.getId() == null) {
            log.error("Film id is not specified");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        Film existingFilm = films.get(newFilm.getId());
        if (existingFilm == null) {
            log.error("Film with id {} not found", newFilm.getId());
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }
        if (newFilm.getName() != null) {
            existingFilm.setName(newFilm.getName());
        }
        if (newFilm.getDescription() != null) {
            existingFilm.setDescription(newFilm.getDescription());
        }
        if (newFilm.getReleaseDate() != null) {
            existingFilm.setReleaseDate(newFilm.getReleaseDate());
        }
        if (newFilm.getDuration() != null) {
            existingFilm.setDurationInSeconds(newFilm.getDurationInSeconds());
        }
        log.info("Film updated: {}", existingFilm);
        return existingFilm;
    }

    private void validateFilm(Film film) {
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.error("Invalid description: {}", film.getDescription());
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Invalid name: {}", film.getName());
            throw new ConditionsNotMetException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Description too long: {}", film.getDescription().length());
            throw new ConditionsNotMetException("Максимальная длина описания — 200 символов");
        }
        LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(earliestReleaseDate)) {
            log.error("Invalid release date: {}", film.getReleaseDate());
            throw new ConditionsNotMetException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() == null || film.getDuration().isNegative() || film.getDuration().isZero()) {
            log.error("Invalid duration: {}", film.getDuration());
            throw new ConditionsNotMetException("Продолжительность фильма должна быть положительным числом");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}