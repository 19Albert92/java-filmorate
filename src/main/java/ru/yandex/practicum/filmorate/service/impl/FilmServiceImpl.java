package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film addLike(Long id, Long userid) {

        User findUser = userStorage.findById(userid);

        Film findFilm = filmStorage.findById(id);

        findFilm.toggleLikes(findUser.getId());

        return filmStorage.update(findFilm);
    }

    @Override
    public Film deleteLike(Long id, Long userid) {

        User findUser = userStorage.findById(userid);

        Film findFilm = filmStorage.findById(id);

        findFilm.toggleLikes(findUser.getId());

        return filmStorage.update(findFilm);
    }

    @Override
    public Collection<Film> getPopularFilmByLikes(Integer count) {

        Comparator<Film> comparatorSortByLikes = Comparator.comparingInt(film -> film.getLikes().size());

        return filmStorage.findAll().stream()
                .filter(film -> film.getLikes() != null)
                .sorted(comparatorSortByLikes)
                .skip(count)
                .toList();
    }

    @Override
    public Film findById(Long id) {
        return filmStorage.findById(id);
    }

    @Override
    public Film create(Film data) {
        return filmStorage.create(data);
    }

    @Override
    public Film update(Film data) {

        Film findFilm = filmStorage.findById(data.getId());

        findFilm.setName(data.getName());
        findFilm.setDescription(data.getDescription());
        findFilm.setDuration(data.getDuration());
        findFilm.setReleaseDate(data.getReleaseDate());

        return filmStorage.update(findFilm);
    }

    @Override
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }
}
