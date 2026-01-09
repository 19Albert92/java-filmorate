package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.OnUpdate;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController extends BaseController<Film> {

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {

        film.setId(super.add(film));

        logger.debug("Фильм успешно добавлен: {}", film);

        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Validated(OnUpdate.class) Film film) {

        Film findFilm = super.findById(film.getId());

        if (findFilm == null) {
            logger.debug("Фильм с таким id -> {} не был найден", film.getId());
            throw new NotFoundException(String.format("Фильм с таким id:%s не был найден", film.getId()));
        }

        findFilm.setName(film.getName());
        logger.trace("Обновлен название на {}", film.getName());

        findFilm.setDescription(film.getDescription());
        logger.trace("Обновлен описание на {}", film.getDescription());

        findFilm.setDuration(film.getDuration());
        logger.trace("Обновлен длительность фильма на {}", film.getDuration());

        findFilm.setReleaseDate(film.getReleaseDate());
        logger.trace("Обновлено дата релиза фильма на {}", film.getReleaseDate());

        logger.info("Фильм успешно обновлен: {}", findFilm);

        return findFilm;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return super.findAll();
    }
}
