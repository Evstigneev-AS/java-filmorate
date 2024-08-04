package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class Film {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Long> userLikes = new HashSet<>();
    private Mpa mpa = new Mpa();
    private List<Genre> genres = new ArrayList<>();
}