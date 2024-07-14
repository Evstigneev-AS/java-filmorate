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

    @GetMapping("/{UserId}")
    public ResponseEntity<User> findById(@PathVariable("UserId") long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{UserId}")
    public void remove(@PathVariable("UserId") long userId) {
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

    @GetMapping("/{UserId}/friends")
    public List<User> findFriends(@PathVariable("UserId") long userId) {
        return userService.findFriends(userId);
    }

    @GetMapping("/{UserId}/friends/common/{OtherId}")
    public List<User> findCommonFriends(@PathVariable("UserId") long userId, @PathVariable("OtherId") long otherId) {
        return userService.findCommonFriends(userId, otherId);
    }

    @PutMapping("/{UserId}/friends/{FriendId}")
    public User addFriend(@PathVariable("UserId") Long userId, @PathVariable("FriendId") Long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{UserId}/friends/{FriendId}")
    public User removeFriend(@PathVariable("UserId") long userId, @PathVariable("FriendId") long friendId) {
        return userService.removeFriend(userId, friendId);
    }
}