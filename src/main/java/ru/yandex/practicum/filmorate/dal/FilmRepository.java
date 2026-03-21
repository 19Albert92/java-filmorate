package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository extends CrudMethodsRepository<Film, Long> {

    List<Film> getPopularFilmByLikes(Integer limit);

    List<Film> getFilmsByDirectorId(Long id, String query);
}
