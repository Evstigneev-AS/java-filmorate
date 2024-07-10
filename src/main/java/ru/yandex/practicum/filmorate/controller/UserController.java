package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @GetMapping("/{userId}")
    public User findPost(@PathVariable("userId") Integer userId) {
        return inMemoryUserStorage.findById(userId).orElseThrow(() ->
                new ConditionsNotMetException("Указанный пост не найден"));
    }

    @DeleteMapping("/{userId}")
    public void remove(@PathVariable("userId") long userId) {
        inMemoryUserStorage.remove(userId);
    }

    @GetMapping
    public Collection<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return inMemoryUserStorage.create(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        return inMemoryUserStorage.update(newUser);
    }

    private void validateUser(User user) {
        inMemoryUserStorage.validateUser(user);
    }
}