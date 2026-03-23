package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmRepository extends CrudMethodsRepository<Film, Long> {

    List<Film> getPopularFilmByLikes(Integer limit);

    List<Film> getRecommendations(Long id);
}
