package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {
        log.debug("Запрос на создание нового пользователя: {}", user);
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException, NotFoundException {
        log.debug("Запрос на изменение пользователя: {}", user);
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) throws NotFoundException {
        log.debug("Запрос на получение информации о пользователе (ID: {}) ", id);
        return userService.getUser(id);
    }

    @GetMapping
    public List<User> getUsers() {
        log.debug("Запрос на получение списка пользователей");
        return userService.getUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) throws NotFoundException {
        log.debug("Запрос на добавление в друзья: userId={}, friendId={}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) throws NotFoundException {
        log.debug("Запрос на удаление из друзей: userId={}, friendId={}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Long id) throws NotFoundException {
        log.debug("Запрос на получение списка друзей пользователя: userId={}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) throws NotFoundException {
        log.debug("Запрос на получение списка общих друзей: userId={}, otherId={}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}