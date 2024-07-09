package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, User> emailToUserMap = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Creating user with email: {}", user.getEmail());
        validateUser(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (emailToUserMap.containsKey(user.getEmail())) {
            log.error("Duplicate email found: {}", user.getEmail());
            throw new DuplicatedDataException("Эта электронная почта уже используется");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        emailToUserMap.put(user.getEmail(), user);
        log.info("User created: {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Updating user with id: {}", newUser.getId());
        if (newUser.getId() == null) {
            log.error("User id is not specified");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        User existingUser = users.get(newUser.getId());
        if (existingUser == null) {
            log.error("User with id {} not found", newUser.getId());
            throw new ConditionsNotMetException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        if (newUser.getEmail() != null && !newUser.getEmail().equals(existingUser.getEmail())) {
            if (emailToUserMap.containsKey(newUser.getEmail())) {
                log.error("Duplicate email found: {}", newUser.getEmail());
                throw new DuplicatedDataException("Эта электронная почта уже используется");
            }
            emailToUserMap.remove(existingUser.getEmail());
            existingUser.setEmail(newUser.getEmail());
            emailToUserMap.put(newUser.getEmail(), existingUser);
        }
        if (newUser.getLogin() != null) {
            existingUser.setLogin(newUser.getLogin());
        }
        if (newUser.getName() != null) {
            existingUser.setName(newUser.getName());
        }
        if (newUser.getBirthday() != null) {
            existingUser.setBirthday(newUser.getBirthday());
        }
        log.info("User updated: {}", existingUser);
        return existingUser;
    }

    public void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Invalid email: {}", user.getEmail());
            throw new ConditionsNotMetException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Invalid login: {}", user.getLogin());
            throw new ConditionsNotMetException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Invalid birthday: {}", user.getBirthday());
            throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}