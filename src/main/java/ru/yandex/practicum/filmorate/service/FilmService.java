package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    @Autowired
    @Qualifier("dbFilmStorage")
    private FilmStorage filmStorage;

    @Autowired
    @Qualifier("dbUserStorage")
    private UserStorage userStorage;

    @Autowired
    @Qualifier("dbMpaStorage")
    private MpaStorage mpaStorage;

    @Autowired
    @Qualifier("dbGenreStorage")
    private GenreStorage genreStorage;

    public Film addFilm(Film film) throws ValidationException {
        log.debug("Запрос на добавление нового фильма: {}", film);
        validate(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException, NotFoundException {
        log.debug("Запрос на изменение фильма: {}", film);
        validate(film);

        Film dbFilm = filmStorage.updateFilm(film);
        if (dbFilm == null) {
            log.info("Фильм с указанным ID {} не найден в базе данных", film.getId());
            throw new NotFoundException(String.format("Фильм с указанным ID (%d) не найден", film.getId()));
        }
        return dbFilm;
    }

    public Film getFilm(Long id) throws NotFoundException {
        log.debug("Запрос на получение информации о фильме (ID: {})", id);
        Film film = filmStorage.getFilm(id);
        if (film == null) {
            throw new NotFoundException(String.format("Фильм с указанным ID (%d) не найден", id));
        }
        return film;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilmsList();
    }

    public void addUserLike(Long filmId, Long userId) throws ValidationException {
        log.debug("Запрос на установку лайка фильму: filmId={}, userId={}", filmId, userId);
        if (filmStorage.getFilm(filmId) == null) {
            log.info("Фильм с указанным ID {} не найден в базе данных", filmId);
            throw new ValidationException(String.format("Фильм с указанным ID %d не найден в базе данных", filmId));
        }
        if (userStorage.getUser(userId) == null) {
            log.info("Пользователь с указанным ID {} не найден в базе данных", userId);
            throw new ValidationException(String.format("Пользователь с указанным ID %d не найден в базе данных", userId));
        }
        filmStorage.addUserLike(filmId, userId);
    }

    public void deleteUserLike(Long filmId, Long userId) throws ValidationException {
        log.debug("Запрос на снятие лайка фильму: filmId={}, userId={}", filmId, userId);
        if (filmStorage.getFilm(filmId) == null) {
            log.info("Фильм с указанным ID {} не найден в базе данных", filmId);
            throw new ValidationException(String.format("Фильм с указанным ID %d не найден в базе данных", filmId));
        }
        if (userStorage.getUser(userId) == null) {
            log.info("Пользователь с указанным ID {} не найден в базе данных", userId);
            throw new ValidationException(String.format("Пользователь с указанным ID %d не найден в базе данных", userId));
        }
        filmStorage.deleteUserLike(filmId, userId);
    }

    public List<Film> getPopular(Integer count) {
        log.debug("Запрос на получение списка популярных фильмов: count={}", count);
        return filmStorage.getPopular(count);
    }

    private void validate(Film film) throws ValidationException {
        log.debug("Валидация фильма: {}", film);
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException(String.format("Не заполнено название фильма: %s", film.getName()));
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException(String.format("Слишком длинное описание: %s", film.getDescription()));
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException(String.format("Некорректная дата релиза: %s", film.getReleaseDate()));
        }
        if (film.getDuration() == null || film.getDuration() <= 0) {
            throw new ValidationException(String.format("Некорректная продолжительность фильма: %s", film.getDuration()));
        }
        if (film.getMpa() != null && film.getMpa().getId() != null) {
            int mpaId = film.getMpa().getId();
            if (mpaStorage.getMpa(mpaId) == null) {
                throw new ValidationException(String.format("MPA-рейтинг с указанным ID %d не найден в базе данных", mpaId));
            }
        }
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (genreStorage.getGenre(genre.getId()) == null) {
                    throw new ValidationException(String.format("Жанр с указанным ID %d не найден в базе данных", genre.getId()));
                }
            }
        }
    }

}
