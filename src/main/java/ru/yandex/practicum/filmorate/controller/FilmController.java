package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.SearchBy;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.model.SortBy;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.util.validate.CommonValidate;

import java.util.Collection;
import java.util.List;

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
            @RequestParam(defaultValue = "20") @Positive Integer limit,
            @RequestParam(defaultValue = "0") Long genreId,
            @RequestParam(defaultValue = "0") Long year
    ) {
        if (genreId != 0 || year != 0) {
            return filmService.getMostPopulars(limit, genreId, year);
        } else {
            CommonValidate.checkNotNullAndPositive(limit, "Параметр count должен быть положительным");

            return filmService.getPopularFilmByLikes(limit);
        }
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
    public void addLike(
            @PathVariable Long id,
            @PathVariable Long userId
    ) {

        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");

        CommonValidate.checkNotNullAndPositive(userId, "Параметр userId должен быть положительным");

        filmService.toggleLike(id, userId, OperationType.ADD);
    }

    @PutMapping
    public FilmDto update(@RequestBody @Valid UpdateFilmRequest film) {

        CommonValidate.checkNotNullAndPositive(film.getId(), "Параметр id должен быть положительным");

        return filmService.update(film);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(
            @PathVariable Long id,
            @PathVariable Long userId
    ) {

        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");

        CommonValidate.checkNotNullAndPositive(userId, "Параметр userId должен быть положительным");

        filmService.toggleLike(id, userId, OperationType.REMOVE);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable Long filmId) {

        CommonValidate.checkNotNullAndPositive(filmId, "Параметр filmId должен быть положительным");

        filmService.delete(filmId);
    }

    @GetMapping("/search")
    public Collection<FilmDto> findFilteredFilms(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) List<SearchBy> by
    ) {

        return filmService.getFilteredFilms(query, by);
    }

    @GetMapping("/director/{directorId}")
    public Collection<FilmDto> findFilmsByDirectorId(
            @PathVariable @Positive Long directorId,
            @RequestParam(required = false) SortBy sortBy
    ) {
        return filmService.getFilmsByDirectorId(directorId, sortBy);
    }

//    @GetMapping("/popular?count={limit}&genreId={genreId}&year={year}")
//    public Collection<FilmDto> findMostPopulars(
//            @PathVariable @Positive Long limit,
//            @PathVariable Long genreId,
//            @PathVariable Long year
//    ) {
//        log.info("\nlimit = {}, genreId = {}, year = {}", limit, genreId, year);
//        return filmService.getMostPopulars(limit, genreId, year);
//    }
}
