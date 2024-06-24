package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTests {

    private UserController userController;
    private User user1;
    private User user2;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();

        user1 = User.builder()
                .name("name1")
                .email("email@test1")
                .login("test1")
                .birthday(LocalDate.of(2017, 7, 7))
                .build();

        user2 = User.builder()
                .name("name2")
                .email("email@test2")
                .login("test2")
                .birthday(LocalDate.of(2008, 6, 6))
                .build();
    }

    @Test
    public void getUsersTest() {
        userController.create(user1);
        userController.create(user2);

        Collection<User> users = userController.findAll();
        assertNotNull(users);
        assertTrue(users.contains(user1));
        assertEquals(2, users.size());
    }

    @Test
    public void createUserUserTest() {
        User newUser1 = userController.create(user1);
        assertTrue(userController.findAll().contains(newUser1));
    }

    @Test
    public void updateUserUserTest() {
        userController.create(user1);
        user2.setId(1L);
        User newUser = userController.update(user2);

        assertTrue(userController.findAll().contains(newUser));
        assertEquals(1, userController.findAll().size());
    }
}