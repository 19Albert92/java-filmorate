package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.util.validate.CommonValidate;
import ru.yandex.practicum.filmorate.util.validate.OnUpdate;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<FilmDto> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{filmId}")
    public FilmDto findFilmById(@PathVariable Long filmId) {

        CommonValidate.checkNotNullAndPositive(filmId, "Параметр filmId должен быть положительным");

        return filmService.findById(filmId);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> findPopularFilms(
            @RequestParam(defaultValue = "10") Integer count
    ) {

        CommonValidate.checkNotNullAndPositive(count, "Параметр count должен быть положительным");

        return filmService.getPopularFilmByLikes(count);
    }

    @GetMapping("/common")
    public Collection<FilmDto> findCommonFilms(
            @RequestParam Long userId,
            @RequestParam Long friendId
    ) {
        CommonValidate.checkNotNullAndPositive(userId, "Параметр userId должен быть положительным");
        CommonValidate.checkNotNullAndPositive(friendId, "Параметр friendId должен быть положительным");

        return filmService.getCommonFilms(userId, friendId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto create(@RequestBody @Valid CreateFilmRequest film) {
        return filmService.create(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean addLike(
            @PathVariable Long id,
            @PathVariable Long userId
    ) {

        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");

        CommonValidate.checkNotNullAndPositive(userId, "Параметр userId должен быть положительным");

        return filmService.toggleLike(id, userId);
    }

    @PutMapping
    public FilmDto update(@RequestBody @Validated({OnUpdate.class}) UpdateFilmRequest film) {

        CommonValidate.checkNotNullAndPositive(film.getId(), "Параметр id должен быть положительным");

        return filmService.update(film);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean deleteLike(
            @PathVariable Long id,
            @PathVariable Long userId
    ) {

        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");

        CommonValidate.checkNotNullAndPositive(userId, "Параметр userId должен быть положительным");

        return filmService.toggleLike(id, userId);
    }
}
