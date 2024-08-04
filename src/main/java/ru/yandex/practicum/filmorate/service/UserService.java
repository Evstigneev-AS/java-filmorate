package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    @Qualifier("dbUserStorage")
    private UserStorage userStorage;

    public User addUser(User user) throws ValidationException {
        log.debug("Запрос на создание нового пользователя: {}", user);
        validate(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) throws ValidationException, NotFoundException {
        log.debug("Запрос на изменение пользователя: {}", user);
        validate(user);
        User dbUser = userStorage.updateUser(user);
        if (dbUser == null) {
            log.info("Пользователь с указанным ID {} не найден", user.getId());
            throw new NotFoundException(String.format("Пользователь с указанным ID (%d) не найден", user.getId()));
        }
        return dbUser;
    }

    public User getUser(Long id) throws NotFoundException {
        User user = userStorage.getUser(id);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с указанным ID (%d) не найден", id));
        }
        return user;
    }

    public List<User> getUsers() {
        return userStorage.getUsersList();
    }

    public void addFriend(Long userId, Long friendId) throws NotFoundException {
        log.debug("Запрос на добавление в друзья: userId={}, friendId={}", userId, friendId);
        getUser(userId);
        getUser(friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) throws NotFoundException {
        log.debug("Запрос на удаление из друзей: userId={}, friendId={}", userId, friendId);
        getUser(userId);
        getUser(friendId);
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Long userId) throws NotFoundException {
        log.debug("Запрос на получение списка друзей пользователя: userId={}", userId);
        getUser(userId);
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) throws NotFoundException {
        log.debug("Запрос на получение списка общих друзей: userId={}, otherId={}", userId, otherId);
        getUser(userId);
        getUser(otherId);
        return userStorage.getCommonFriends(userId, otherId);
    }

    private void validate(User user) throws ValidationException {
        log.debug("Валидация пользователя: {}", user);
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException(String.format("Некорректный адрес электронной почты: %s", user.getEmail()));
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException(String.format("Некорректный логин пользователя: %s", user.getLogin()));
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException(String.format("Некорректная дата рождения: %s", user.getBirthday()));
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
