package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FilmControllerTests {

    private FilmController filmController;
    private FilmService filmService;
    private Film film1;
    private Film film2;

    @BeforeEach
    public void beforeEach() {
        filmService = mock(FilmService.class);
        filmController = new FilmController(filmService);

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
        when(filmService.findAll()).thenReturn(List.of(film1, film2));

        Collection<Film> films = filmController.findAll();
        assertNotNull(films);
        assertTrue(films.contains(film1));
        assertTrue(films.contains(film2));
        assertEquals(2, films.size());

        verify(filmService, times(1)).findAll();
    }

    @Test
    public void createFilmTest() {
        when(filmService.create(film1)).thenReturn(film1);
        when(filmService.findAll()).thenReturn(List.of(film1));

        Film newFilm = filmController.create(film1);
        assertTrue(filmController.findAll().contains(newFilm));

        verify(filmService, times(1)).create(film1);
    }

    @Test
    public void updateFilmTest() {
        when(filmService.create(film1)).thenReturn(film1);
        when(filmService.update(film2)).thenReturn(film2);
        when(filmService.findAll()).thenReturn(List.of(film2));

        filmController.create(film1);
        film2.setId(1L);
        Film updatedFilm = filmController.update(film2);

        assertEquals(film2, updatedFilm);
        assertEquals(1, filmController.findAll().size());
    }

    @Test
    public void findFilmTest() {
        when(filmService.findById(1L)).thenReturn(film1);

        Film foundFilm = filmController.findFilm(1L);
        assertNotNull(foundFilm);
        assertEquals(film1.getName(), foundFilm.getName());

        verify(filmService, times(1)).findById(1L);
    }

    @Test
    public void removeFilmTest() {
        doNothing().when(filmService).remove(1L);

        filmController.remove(1L);
        verify(filmService, times(1)).remove(1L);
    }

    @Test
    public void addLikeTest() {
        when(filmService.addLike(1L, 1L)).thenReturn(film1);

        Film likedFilm = filmController.addLike(1L, 1L);
        assertNotNull(likedFilm);
        assertEquals(film1.getName(), likedFilm.getName());

        verify(filmService, times(1)).addLike(1L, 1L);
    }

    @Test
    public void removeLikeTest() {
        when(filmService.removeLike(1L, 1L)).thenReturn(film1);

        Film unlikedFilm = filmController.removeLike(1L, 1L);
        assertNotNull(unlikedFilm);
        assertEquals(film1.getName(), unlikedFilm.getName());

        verify(filmService, times(1)).removeLike(1L, 1L);
    }
}
