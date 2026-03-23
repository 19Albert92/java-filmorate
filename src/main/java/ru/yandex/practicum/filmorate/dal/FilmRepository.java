package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository extends CrudMethodsRepository<Film, Long> {

    List<Film> getPopularFilmByLikes(Integer limit);

    List<Film> getCommonFilms(Long userId, Long friendId);

    List<Film> getFilmsByDirectorId(Long id);

    List<Film> getFilmsByDirectorIdSortedByYear(Long id);

    List<Film> getFilmsByDirectorIdSortedByLikes(Long id);
}
