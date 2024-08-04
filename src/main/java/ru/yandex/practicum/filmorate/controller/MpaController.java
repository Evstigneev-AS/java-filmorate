package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> getMpaList() {
        log.debug("Запрос на получение списка MPA-рейтингов");
        return mpaService.getMpaList();
    }

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable Integer id) throws NotFoundException {
        log.debug("Запрос на получение информации о MPA-рейтинге по идентификатору (ID: {})", id);
        return mpaService.getMpa(id);
    }
}