package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.LikeRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {

    private final GenreService genreService;

    private final FilmRepository filmStorage;

    private final UserRepository userStorage;

    private final MpaRepository mpaStorage;

    private final LikeRepository likeStorage;

    public FilmServiceImpl(GenreService genreService, FilmRepository filmStorage,
                           UserRepository userStorage, MpaRepository mpaStorage, LikeRepository likeStorage) {
        this.genreService = genreService;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
        this.likeStorage = likeStorage;
    }

    @Override
    @Transactional
    public boolean toggleLike(Long filmId, Long userid) {

        userStorage.findById(userid)
                .orElseThrow(() -> new NotFoundException("Пользователя с данным id нет"));

        filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с данным id нет"));

        boolean isExists = likeStorage.isLikeExists(filmId, userid);

        if (isExists) {
            likeStorage.deleteLike(filmId, userid);
        } else {
            likeStorage.addLike(filmId, userid);
        }

        return true;
    }

    @Override
    public Collection<FilmDto> getPopularFilmByLikes(Integer count) {
        return filmStorage.getPopularFilmByLikes(count).stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    @Override
    public FilmDto findById(Long id) {
        return filmStorage.findById(id)
                .map(FilmMapper::mapToFilmDto)
                .map(dto -> {
                    dto.setGenres(genreService.getGenresByFilmId(dto.getId()));
                    return dto;
                })
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    @Override
    @Transactional
    public FilmDto create(CreateFilmRequest request) {

        Film film = FilmMapper.mapToFilm(request);

        Set<Integer> uniqGenres = new HashSet<>();

        if (request.getGenres() != null && !request.getGenres().isEmpty()) {
            uniqGenres = request.getGenres().stream()
                    .map(Genre::getId)
                    .filter(genre_id -> genreService.getById(genre_id) != null)
                    .collect(Collectors.toSet());
        }

        if (request.getMpa() != null) {
            mpaStorage.findById(request.getMpa().getId())
                    .orElseThrow(() -> new NotFoundException("Такого рейтинга нет"));
        }

        film = filmStorage.create(film);

        if (!uniqGenres.isEmpty()) {
            genreService.saveGenres(film.getId(), uniqGenres);

            film.setGenres(request.getGenres());
        }

        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto update(UpdateFilmRequest request) {

        Film film = filmStorage.findById(request.getId())
                .map(f -> FilmMapper.mapToUpdateFields(f, request))
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        film = filmStorage.update(film);

        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public Collection<FilmDto> findAll() {
        return filmStorage.findAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
