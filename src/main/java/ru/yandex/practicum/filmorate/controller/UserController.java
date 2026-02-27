package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.user.CreateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.util.validate.CommonValidate;
import ru.yandex.practicum.filmorate.util.validate.OnUpdate;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto findById(
            @PathVariable Long id
    ) {

        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");

        return userService.findById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDto> getFiendsByUser(
            @PathVariable Long id
    ) {

        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");

        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserDto> getCommonFriends(
            @PathVariable Long id,
            @PathVariable Long otherId
    ) {
        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");

        CommonValidate.checkNotNullAndPositive(otherId, "Параметр otherId должен быть положительным");

        return userService.getAllCommonFriends(id, otherId);
    }

    @PostMapping
    public UserDto create(@RequestBody @Valid CreateUserRequest user) {
        return userService.create(user);
    }

    @PutMapping
    public UserDto update(
            @RequestBody @Validated({OnUpdate.class}) UpdateUserRequest user
    ) {
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<UserDto> addFriend(
            @PathVariable Long id,
            @PathVariable Long friendId
    ) {

        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");

        CommonValidate.checkNotNullAndPositive(friendId, "Параметр friendId должен быть положительным");

        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<UserDto> deleteFriend(
            @PathVariable Long id,
            @PathVariable Long friendId
    ) {

        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");

        CommonValidate.checkNotNullAndPositive(friendId, "Параметр id должен быть положительным");

        return userService.deleteFriend(id, friendId);
    }
}
