package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    void save(List<Object[]> genres);

    Optional<Genre> findById(Integer genreId);

    List<Genre> findAll();

    List<Genre> findByFilmId(Long filmId);
}
