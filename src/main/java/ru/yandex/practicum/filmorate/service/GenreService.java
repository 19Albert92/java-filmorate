package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> saveGenres(Long filmId, List<Genre> genresList);

    Genre getById(Integer genreId);

    List<Genre> getAllGenres();

    List<Genre> getGenresByFilmId(Long filmId);
}
