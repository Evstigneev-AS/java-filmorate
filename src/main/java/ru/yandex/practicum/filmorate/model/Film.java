package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    Set<Long> userLikes = new HashSet<>();
    Mpa mpa = new Mpa();
    List<Genre> genres = new ArrayList<>();
}