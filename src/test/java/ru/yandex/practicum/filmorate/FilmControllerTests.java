package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTests {

    private FilmController filmController;
    private Film film1;
    private Film film2;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();

        film1 = Film.builder()
                .name("name1")
                .description("description1")
                .releaseDate(LocalDate.of(2017, 7, 7))
                .duration(Duration.ofMinutes(90))
                .build();

        film2 = Film.builder()
                .name("name2")
                .description("description2")
                .releaseDate(LocalDate.of(2008, 6, 6))
                .duration(Duration.ofMinutes(60))
                .build();
    }

    @Test
    public void getFilmsTest() {
        filmController.create(film1);
        filmController.create(film2);

        Collection<Film> films = filmController.findAll();
        assertNotNull(films);
        assertTrue(films.contains(film2));
        assertEquals(2, films.size());
    }

    @Test
    public void createFilmTest() {
        Film newFilm = filmController.create(film1);
        assertTrue(filmController.findAll().contains(newFilm));
    }

    @Test
    public void updateFilmTest() {
        filmController.create(film1);
        film2.setId(1L);
        Film newFilm = filmController.update(film2);

        assertEquals(film2, newFilm);
        assertEquals(1, filmController.findAll().size());
    }
}
