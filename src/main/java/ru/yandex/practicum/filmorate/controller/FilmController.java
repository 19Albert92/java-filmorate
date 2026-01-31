package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.util.validate.CommonValidate;
import ru.yandex.practicum.filmorate.util.validate.OnUpdate;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("popular")
    public Collection<Film> findPopularFilms(
            @RequestParam(defaultValue = "10") Integer count
    ) {

        CommonValidate.checkNotNegative(count, "Параметр count должен быть положительным");

        return filmService.getPopularFilmByLikes(count);
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        return filmService.create(film);
    }
    @PutMapping("/{id}/like/{userId}")
    public Film addLike(
            @PathVariable Long id,
            @PathVariable Long userId
    ) {

        CommonValidate.checkNotNegative(id, "Параметр id должен быть положительным");

        CommonValidate.checkNotNegative(userId, "Параметр userId должен быть положительным");

        return filmService.addLike(id, userId);
    }

    @PutMapping
    public Film update(@RequestBody @Validated({OnUpdate.class, Default.class}) Film film) {
        return filmService.update(film);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(
            @PathVariable Long id,
            @PathVariable Long userId
    ) {

        CommonValidate.checkNotNegative(id, "Параметр id должен быть положительным");

        CommonValidate.checkNotNegative(userId, "Параметр userId должен быть положительным");

        return filmService.deleteLike(id, userId);
    }
}
