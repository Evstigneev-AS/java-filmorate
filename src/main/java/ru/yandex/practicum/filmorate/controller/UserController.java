package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findById(@PathVariable("userId") long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public void remove(@PathVariable("userId") long userId) {
        userService.remove(userId);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User newUser) {
        return userService.update(newUser);
    }

    @GetMapping("/{userId}/friends")
    public List<User> findFriends(@PathVariable("userId") long userId) {
        return userService.findFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable("userId") long userId, @PathVariable("otherId") long otherId) {
        return userService.findCommonFriends(userId, otherId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User removeFriend(@PathVariable("userId") long userId, @PathVariable("friendId") long friendId) {
        return userService.removeFriend(userId, friendId);
    }
}