package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
//import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
//@EqualsAndHashCode(of = {"email"})
public class User {

    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    Set<Long> friends = new HashSet<>();
}