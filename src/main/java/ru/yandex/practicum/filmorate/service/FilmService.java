package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.film.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;

import java.util.Collection;

public interface FilmService {

    FilmDto findById(Long id);

    FilmDto create(CreateFilmRequest data);

    FilmDto update(UpdateFilmRequest data);

    Collection<FilmDto> findAll();

    boolean toggleLike(Long filmId, Long userid);

    Collection<FilmDto> getPopularFilmByLikes(Integer count);
}
