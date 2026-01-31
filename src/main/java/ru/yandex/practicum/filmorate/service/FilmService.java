package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.CrudMethodsStorage;

import java.util.Collection;

public interface FilmService extends CrudMethodsStorage<Film, Long> {

    Film addLike(Long id, Long userid);

    Film deleteLike(Long id, Long userid);

    Collection<Film> getPopularFilmByLikes(Integer count);
}
