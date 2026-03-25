package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
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
import ru.yandex.practicum.filmorate.model.SearchBy;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;
import java.util.Collections;
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
    public void toggleLike(Long filmId, Long userid, OperationType operation) {

        userStorage.findById(userid)
                .orElseThrow(() -> new NotFoundException("Пользователя с данным id нет"));

        filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с данным id нет"));

        boolean isExists = likeStorage.isLikeExists(filmId, userid);

        FeedDto feedDto = new FeedDto();
        feedDto.setEventType(EventType.LIKE);
        feedDto.setEntityId(filmId);
        feedDto.setUserId(userid);
        feedDto.setOperation(operation);
        feedService.addFeed(feedDto);

        if (operation.equals(OperationType.REMOVE)) {
            likeStorage.deleteLike(filmId, userid);
        }

        if (operation.equals(OperationType.ADD) && !isExists) {
            likeStorage.addLike(filmId, userid);
        }
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
    public Collection<FilmDto> getFilteredFilms(String query, List<SearchBy> by) {
        if (by == null || by.isEmpty() || query == null || query.isBlank()) {
            return filmStorage.getPopularFilmByLikes().stream()
                    .map(FilmMapper::mapToFilmDto)
                    .peek(film -> {
                        film.setGenres(genreService.getGenresByFilmId(film.getId()));
                        film.setDirectors(directorService.findDirectorsByFilmId(film.getId()));
                    })
                    .toList();
        }

        List<Film> filteredFilms;

        List<String> byParams = by.stream()
                .map(e -> e.name().toLowerCase())
                .toList();

        if (byParams.contains(SearchBy.title.name()) && byParams.contains(SearchBy.director.name())) {
            filteredFilms = filmStorage.getFilteredByTitleAndDirectorFilms(query);
        } else if (byParams.contains(SearchBy.title.name())) {
            filteredFilms = filmStorage.getFilteredByTitleFilms(query);
        } else if (byParams.contains(SearchBy.director.name())) {
            filteredFilms = filmStorage.getFilteredByDirectorFilms(query);
        } else {
            filteredFilms = filmStorage.getPopularFilmByLikes();
        }

        return filteredFilms.stream()
                .map(FilmMapper::mapToFilmDto)
                .peek(film -> {
                    film.setGenres(genreService.getGenresByFilmId(film.getId()));
                    film.setDirectors(directorService.findDirectorsByFilmId(film.getId()));
                })
                .toList();
    }

    @Override
    public Collection<FilmDto> getCommonFilms(Long userId, Long friendId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с данным id нет"));

        userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователя с данным id нет"));

        return filmStorage.getCommonFilms(userId, friendId).stream()
                .map(FilmMapper::mapToFilmDto)
                .peek(film -> film.setGenres(genreService.getGenresByFilmId(film.getId())))
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

        Set<Long> uniqDirectors = new HashSet<>();

        if (request.getMpa() != null) {
            mpaStorage.findById(request.getMpa().getId())
                    .orElseThrow(() -> new NotFoundException("Такого рейтинга нет"));
        }

        if (request.getDirectors() != null && !request.getDirectors().isEmpty()) {
            uniqDirectors = request.getDirectors().stream()
                    .map(Director::getId)
                    .filter(directorId -> directorService.findById(directorId) != null)
                    .collect(Collectors.toSet());
        }

        film = filmStorage.create(film);

        if (!uniqDirectors.isEmpty()) {
            directorService.saveDirectors(film.getId(), uniqDirectors);

            film.setDirectors(request.getDirectors());
        }

        if (request.getGenres() != null) {

            List<Genre> uniqGenres = genreService.saveGenres(film.getId(), request.getGenres());

            film.setGenres(uniqGenres);
        }

        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    @Transactional
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

        if (request.getGenres() != null) {

            List<Genre> uniqGenres = genreService.saveGenres(film.getId(), request.getGenres());

            film.setGenres(uniqGenres);
        }

        if (request.getMpa() != null) {
            mpaStorage.findById(request.getMpa().getId())
                    .orElseThrow(() -> new NotFoundException("Такого рейтинга нет"));
        }

        film = filmStorage.update(film);

        directorStorage.removeFilmDirectors(film.getId());

        if (!uniqDirectors.isEmpty()) {
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
        checkDirectorExists(id);

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

    @Override
    public Collection<FilmDto> getRecommendations(Long id) {
        userStorage.findById(id).orElseThrow(() -> new NotFoundException("User is not found"));

        try {
            return filmStorage.getRecommendations(id).stream()
                    .map(FilmMapper::mapToFilmDto)
                    .peek(dto -> dto.setGenres(genreService.getGenresByFilmId(dto.getId())))
                    .toList();
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<FilmDto> getMostPopulars(Integer count, Long genreId, Long year) {
        try {
            return filmStorage.getMostPopulars(count, genreId, year).stream()
                    .map(FilmMapper::mapToFilmDto)
                    .peek(dto -> dto.setGenres(genreService.getGenresByFilmId(dto.getId())))
                    .toList();
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public void delete(Long filmId) {
        filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        filmStorage.deleteById(filmId);
    }

    private void checkDirectorExists(Long directorId) {
        directorStorage.findById(directorId)
                .orElseThrow(() -> new NotFoundException("Режиссер не найден"));
    }
}

