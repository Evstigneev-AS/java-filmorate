package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@Primary
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Optional<Film> findById(long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Film create(Film film) {
        validateFilm(film);
        film.setId(getNextId());
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        validateFilm(newFilm);
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public void remove(long filmId) {
        films.remove(filmId);
    }

    @Override
    public List<Film> findPopularFilm(Comparator<Film> comparator, long count) {
        return films.values().stream()
                .sorted(comparator)
                .limit(count)
                .toList();
    }

    public void validateFilm(Film film) {
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.error("Invalid description: {}", film.getDescription());
            throw new NotFoundException("Описание не может быть пустым");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Invalid name: {}", film.getName());
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Description too long: {}", film.getDescription().length());
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(earliestReleaseDate)) {
            log.error("Invalid release date: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() == null || film.getDuration().isNegative() || film.getDuration().isZero()) {
            log.error("Invalid duration: {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
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