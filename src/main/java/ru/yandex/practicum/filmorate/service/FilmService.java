package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.film.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.model.SortBy;
import ru.yandex.practicum.filmorate.model.SearchBy;

import java.util.Collection;
import java.util.List;

public interface FilmService {

    FilmDto findById(Long id);

    FilmDto create(CreateFilmRequest data);

    FilmDto update(UpdateFilmRequest data);

    Collection<FilmDto> findAll();

    void toggleLike(Long filmId, Long userid, OperationType operation);

    Collection<FilmDto> getPopularFilmByLikes(Integer count);

    Collection<FilmDto> getRecommendations(Long id);

    Collection<FilmDto> getFilteredFilms(String query, List<SearchBy> by);

    Collection<FilmDto> getCommonFilms(Long userId, Long friendId);

    Collection<FilmDto> getFilmsByDirectorId(Long id, SortBy sortBy);

    void delete(Long filmId);
}
