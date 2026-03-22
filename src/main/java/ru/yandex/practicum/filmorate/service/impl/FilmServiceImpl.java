package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.dto.feed.FeedDto;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {

    private final GenreService genreService;

    private final DirectorService directorService;

    private final FilmRepository filmStorage;

    private final FeedService feedService;

    private final UserRepository userStorage;

    private final MpaRepository mpaStorage;

    private final LikeRepository likeStorage;

    private final DirectorRepository directorStorage;

    public FilmServiceImpl(
            GenreService genreService,
            DirectorService directorService,
            FilmRepository filmStorage,
            FeedService feedService,
            UserRepository userStorage,
            MpaRepository mpaStorage,
            LikeRepository likeStorage,
            DirectorRepository directorStorage) {
        this.genreService = genreService;
        this.directorService = directorService;
        this.filmStorage = filmStorage;
        this.feedService = feedService;
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
        this.likeStorage = likeStorage;
        this.directorStorage = directorStorage;
    }

    @Override
    @Transactional
    public boolean toggleLike(Long filmId, Long userid) {

        userStorage.findById(userid)
                .orElseThrow(() -> new NotFoundException("Пользователя с данным id нет"));

        filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с данным id нет"));

        boolean isExists = likeStorage.isLikeExists(filmId, userid);

        FeedDto feedDto = new FeedDto();
        feedDto.setEventType(EventType.LIKE);
        feedDto.setEntityId(filmId);
        feedDto.setUserId(userid);

        if (isExists) {
            likeStorage.deleteLike(filmId, userid);
            feedDto.setOperation(OperationType.REMOVE);
        } else {
            likeStorage.addLike(filmId, userid);
            feedDto.setOperation(OperationType.ADD);
        }

        feedService.addFeed(feedDto);

        return true;
    }

    @Override
    public Collection<FilmDto> getPopularFilmByLikes(Integer count) {
        return filmStorage.getPopularFilmByLikes(count).stream()
                .map(FilmMapper::mapToFilmDto)
                .peek(film -> {
                    film.setGenres(genreService.getGenresByFilmId(film.getId()));
                    film.setDirectors(directorService.findDirectorsByFilmId(film.getId()));
                })
                .toList();
    }

    @Override
    public FilmDto findById(Long id) {
        return filmStorage.findById(id)
                .map(FilmMapper::mapToFilmDto)
                .map(dto -> {
                    dto.setGenres(genreService.getGenresByFilmId(dto.getId()));
                    dto.setDirectors(directorService.findDirectorsByFilmId(dto.getId()));
                    return dto;
                })
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    @Override
    @Transactional
    public FilmDto create(CreateFilmRequest request) {

        Film film = FilmMapper.mapToFilm(request);

        Set<Integer> uniqGenres = new HashSet<>();

        Set<Long> uniqDirectors = new HashSet<>();

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

        if (request.getDirector() != null && !request.getDirector().isEmpty()) {
            uniqDirectors = request.getDirector().stream()
                    .map(Director::getId)
                    .filter(directorId -> directorService.findById(directorId) != null)
                    .collect(Collectors.toSet());
        }

        film = filmStorage.create(film);

        if (!uniqGenres.isEmpty()) {
            genreService.saveGenres(film.getId(), uniqGenres);

            film.setGenres(request.getGenres());
        }

        if (!uniqDirectors.isEmpty()) {
            directorService.saveDirectors(film.getId(), uniqDirectors);

            film.setDirectors(request.getDirector());
        }

        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto update(UpdateFilmRequest request) {

        Film film = filmStorage.findById(request.getId())
                .map(f -> FilmMapper.mapToUpdateFields(f, request))
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        Set<Long> uniqDirectors = new HashSet<>();

        if (request.hasDirector()) {
            uniqDirectors = request.getDirectors().stream()
                    .map(Director::getId)
                    .filter(directorId -> directorService.findById(directorId) != null)
                    .collect(Collectors.toSet());
        }

        film = filmStorage.update(film);

        if (!uniqDirectors.isEmpty()) {
            directorStorage.removeFilmDirectors(film.getId());
            directorService.saveDirectors(film.getId(), uniqDirectors);

            film.setDirectors(request.getDirectors());
        }

        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public Collection<FilmDto> findAll() {
        return filmStorage.findAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .peek(dto -> {
                    dto.setGenres(genreService.getGenresByFilmId(dto.getId()));
                    dto.setDirectors(directorService.findDirectorsByFilmId(dto.getId()));
                })
                .toList();
    }

    @Override
    public Collection<FilmDto> getFilmsByDirectorId(Long id, SortBy sortBy) {
        String query = (sortBy != null) ? sortBy.name() : SortBy.defaultSort.name();

        List<Film> returnedFilms;

        switch (query) {
            case "year" -> returnedFilms = filmStorage.getFilmsByDirectorIdSortedByYear(id);
            case "likes" -> returnedFilms = filmStorage.getFilmsByDirectorIdSortedByLikes(id);
            default -> returnedFilms = filmStorage.getFilmsByDirectorId(id);
        }

        return returnedFilms.stream()
                .map(FilmMapper::mapToFilmDto)
                .peek(dto -> {
                    dto.setGenres(genreService.getGenresByFilmId(dto.getId()));
                    dto.setDirectors(directorService.findDirectorsByFilmId(dto.getId()));
                })
                .toList();
    }
}


