package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FilmStorageTest {
    private final InMemoryFilmStorage filmStorage;

    public FilmStorageTest() {
        filmStorage = new InMemoryFilmStorage();
    }

    private Film newDefaultFilm() {
        Film film = new Film();
        film.setName("Film Name");
        film.setReleaseDate(LocalDate.of(2001, 1, 1));
        film.setDuration(120);
        return film;
    }

    public Film getFilm(Long id) throws NotFoundException {
        Film film = filmStorage.getFilm(id);
        if (film == null) {
            throw new NotFoundException(String.format("Фильм с указанным ID (%d) не найден", id));
        }
        return film;
    }

    private void checkEqualsFilms(Film oldFilm, Film newFilm) {
        assertNotNull(newFilm);
        assertEquals(oldFilm.getName(), newFilm.getName());
        assertEquals(
                (oldFilm.getDescription() != null) ? oldFilm.getDescription() : "",
                (newFilm.getDescription() != null) ? newFilm.getDescription() : "");
        assertEquals(oldFilm.getReleaseDate(), newFilm.getReleaseDate());
        assertEquals(oldFilm.getDuration(), newFilm.getDuration());
    }

    @Test
    public void addFilm_addToList() throws NotFoundException {
        Film film = newDefaultFilm();

        Film newFilm;
        newFilm = filmStorage.addFilm(film);
        checkEqualsFilms(film, newFilm);

        Film savedFilm = getFilm(newFilm.getId());
        checkEqualsFilms(film, savedFilm);
    }

    @Test
    public void updateFilm_updateList() throws NotFoundException {
        Long filmId;
        filmId = filmStorage.addFilm(newDefaultFilm()).getId();

        Film film = newDefaultFilm();
        film.setId(filmId);
        film.setName("Просто хороший фильм");
        film.setDescription("Описание к хорошему фильму");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2024, 1, 1));

        Film updFilm = filmStorage.updateFilm(film);
        if (updFilm == null) {
            throw new RuntimeException("Фильм не найден!");
        }
        checkEqualsFilms(film, updFilm);

        Film savedFilm = getFilm(filmId);
        checkEqualsFilms(film, savedFilm);
    }
}