package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreStorage;

    public GenreServiceImpl(GenreRepository genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Genre> saveGenres(Long filmId, List<Genre> genresList) {

        genreStorage.clearGenresByFilmId(filmId);

        Set<Integer> uniqGenres = genresList.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        for (Integer genreId : uniqGenres) {
            genreStorage
                    .findById(genreId)
                    .orElseThrow(() -> new NotFoundException("Такого рейтинга нет"));
        }

        List<Object[]> genres = uniqGenres.stream()
                .map(id -> new Object[]{filmId, id}).toList();

        genreStorage.save(genres);

        return uniqGenres.stream()
                .map(id -> new Genre(id, null))
                .collect(Collectors.toList());
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
