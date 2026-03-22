package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorRepository extends CrudMethodsRepository<Director, Long> {
    void save(List<Object[]> directors);

    boolean delete(Long id);

    List<Director> findDirectorsByFilmId(Long id);

    void removeFilmDirectors(Long filmId);
}
