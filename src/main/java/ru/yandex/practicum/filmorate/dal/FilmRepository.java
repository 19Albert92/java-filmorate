package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository extends CrudMethodsRepository<Film, Long> {

    List<Film> getPopularFilmByLikes(Integer limit);

    List<Film> getRecommendations(Long id);

    List<Film> getPopularFilmByLikes();

    List<Film> getFilteredByTitleAndDirectorFilms(String query);

    List<Film> getFilteredByTitleFilms(String query);

    List<Film> getFilteredByDirectorFilms(String query);

    List<Film> getCommonFilms(Long userId, Long friendId);

    List<Film> getFilmsByDirectorId(Long id);

    List<Film> getFilmsByDirectorIdSortedByYear(Long id);

    List<Film> getFilmsByDirectorIdSortedByLikes(Long id);

    List<Film> getMostPopulars(Integer count, Long genreId, Long year);

    void deleteById(Long id);
}
