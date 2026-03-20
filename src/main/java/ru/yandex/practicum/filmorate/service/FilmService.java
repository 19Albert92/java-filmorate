package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.film.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.SearchBy;

import java.util.Collection;
import java.util.List;

public interface FilmService {

    FilmDto findById(Long id);

    FilmDto create(CreateFilmRequest data);

    FilmDto update(UpdateFilmRequest data);

    Collection<FilmDto> findAll();

    boolean toggleLike(Long filmId, Long userid);

    Collection<FilmDto> getPopularFilmByLikes(Integer count);

    Collection<FilmDto> getFilteredFilms(String query, List<SearchBy> by);
}
