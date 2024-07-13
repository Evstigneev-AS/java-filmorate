package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTests {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    public void beforeEach() {
        userService = mock(UserService.class);
        userController = new UserController(userService);

        user1 = User.builder()
                .name("User1")
                .email("user1@example.com")
                .login("user1login")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        user2 = User.builder()
                .name("User2")
                .email("user2@example.com")
                .login("user2login")
                .birthday(LocalDate.of(1992, 2, 2))
                .build();
    }

    @Test
    public void getUsersTest() {
        when(userService.findAll()).thenReturn(List.of(user1, user2));

        List<User> users = userController.findAll();
        assertNotNull(users);
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        assertEquals(2, users.size());

        verify(userService, times(1)).findAll();
    }

    @Test
    public void createUserTest() {
        when(userService.create(any(User.class))).thenReturn(user1);

        User newUser = userController.create(user1);
        assertNotNull(newUser);
        assertEquals(user1.getName(), newUser.getName());

        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    public void updateUserTest() {
        when(userService.update(any(User.class))).thenReturn(user2);

        User updatedUser = userController.update(user2);
        assertNotNull(updatedUser);
        assertEquals(user2.getName(), updatedUser.getName());

        verify(userService, times(1)).update(any(User.class));
    }

    @Test
    public void findUserByIdTest() {
        when(userService.findById(1L)).thenReturn(user1);

        ResponseEntity<User> foundUser = userController.findById(1L);
        assertNotNull(foundUser);
        assertEquals(user1.getName(), foundUser.getBody().getName());

        verify(userService, times(1)).findById(1L);
    }

    @Test
    public void removeUserTest() {
        doNothing().when(userService).remove(1L);

        userController.remove(1L);
        verify(userService, times(1)).remove(1L);
    }

    @Test
    public void addFriendTest() {
        when(userService.addFriend(1L, 2L)).thenReturn(user1);

        User userWithFriend = userController.addFriend(1L, 2L);
        assertNotNull(userWithFriend);
        assertEquals(user1.getName(), userWithFriend.getName());

        verify(userService, times(1)).addFriend(1L, 2L);
    }

    @Test
    public void removeFriendTest() {
        when(userService.removeFriend(1L, 2L)).thenReturn(user1);

        User userWithoutFriend = userController.removeFriend(1L, 2L);
        assertNotNull(userWithoutFriend);
        assertEquals(user1.getName(), userWithoutFriend.getName());

        verify(userService, times(1)).removeFriend(1L, 2L);
    }

    @Test
    public void findFriendsTest() {
        when(userService.findFriends(1L)).thenReturn(List.of(user2));

        List<User> friends = userController.findFriends(1L);
        assertNotNull(friends);
        assertTrue(friends.contains(user2));

        verify(userService, times(1)).findFriends(1L);
    }

    @Test
    public void findCommonFriendsTest() {
        when(userService.findCommonFriends(1L, 2L)).thenReturn(List.of(user2));

        List<User> commonFriends = userController.findCommonFriends(1L, 2L);
        assertNotNull(commonFriends);
        assertTrue(commonFriends.contains(user2));

        verify(userService, times(1)).findCommonFriends(1L, 2L);
    }
}