package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreService {

    void saveGenres(Long filmId, Set<Integer> uniqGenres);

    Genre getById(Integer genreId);

    List<Genre> getAllGenres();

    List<Genre> getGenresByFilmId(Long filmId);
}
