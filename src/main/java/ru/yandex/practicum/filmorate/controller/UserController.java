package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.utill.OnUpdate;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User> {

    @PostMapping
    public User create(@RequestBody @Valid User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            logger.trace("В username записался login из за отсутствие");
            user.setName(user.getLogin());
        }

        user.setId(super.add(user));

        logger.debug("Пользователь успешно добавлен: {}", user);

        return user;
    }

    @PutMapping
    public User update(@RequestBody @Validated(OnUpdate.class) User user) {

        User findUser = super.findById(user.getId());

        if (findUser == null) {
            logger.debug("Пользователь с таким id -> {} не был найден", user.getId());
            throw new NotFoundException(String.format("Пользователь с таким id:%s не был найден", user.getId()));
        }

        findUser.setEmail(user.getEmail());
        logger.trace("Обновлен email на {}", user.getEmail());

        findUser.setLogin(user.getLogin());
        logger.trace("Обновлен login на {}", user.getLogin());

        if ((user.getName() == null || !user.getName().isBlank()) && findUser.getName().equals(findUser.getLogin())) {
            findUser.setName(user.getLogin());
            logger.trace("Обновлен name на {}", user.getName());
        } else {
            findUser.setName(user.getName());
            logger.trace("Обновлен name на {}", user.getName());
        }

        findUser.setBirthday(user.getBirthday());
        logger.trace("Обновлен birthday на {}", user.getBirthday());

        logger.info("Пользователь успешно обновлен: {}", findUser);

        return findUser;
    }

    @GetMapping
    public Collection<User> findAll() {
        return super.findAll();
    }
}
