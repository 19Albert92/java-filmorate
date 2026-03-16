package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.dal.GenreRepository;

import java.util.List;
import java.util.Set;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreStorage;

    public GenreServiceImpl(GenreRepository genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public void saveGenres(Long filmId, Set<Integer> uniqGenres) {

        List<Object[]> genres = uniqGenres.stream().map(id -> new Object[]{filmId, id}).toList();

        genreStorage.save(genres);
    }

    @Override
    public Genre getById(Integer genreId) {
        return genreStorage.findById(genreId)
                .orElseThrow(() -> new NotFoundException("Такого жанра нет"));
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreStorage.findAll();
    }

    @Override
    public List<Genre> getGenresByFilmId(Long filmId) {
        return genreStorage.findByFilmId(filmId);
    }
}
