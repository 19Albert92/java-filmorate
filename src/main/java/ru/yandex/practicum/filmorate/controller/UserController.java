package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.util.validate.CommonValidate;
import ru.yandex.practicum.filmorate.util.validate.OnUpdate;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(
            @PathVariable Long id
    ) {

        CommonValidate.checkNotNegative(id, "Параметр id должен быть положительным");

        return userService.findById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFiendsByUser(
            @PathVariable Long id
    ) {

        CommonValidate.checkNotNegative(id, "Параметр id должен быть положительным");

        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(
            @PathVariable Long id,
            @PathVariable Long otherId
    ) {
        CommonValidate.checkNotNegative(id, "Параметр id должен быть положительным");

        CommonValidate.checkNotNegative(otherId, "Параметр otherId должен быть положительным");

        return userService.getAllCommonFriends(id, otherId);
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(
            @RequestBody @Validated({OnUpdate.class, Default.class}) User user
    ) {
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(
            @PathVariable Long id,
            @PathVariable Long friendId
    ) {

        CommonValidate.checkNotNegative(id, "Параметр id должен быть положительным");

        CommonValidate.checkNotNegative(friendId, "Параметр friendId должен быть положительным");

        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(
            @PathVariable Long id,
            @PathVariable Long friendId
    ) {

        CommonValidate.checkNotNegative(id, "Параметр id должен быть положительным");

        CommonValidate.checkNotNegative(friendId, "Параметр id должен быть положительным");

        return userService.deleteFriend(id, friendId);
    }
}
