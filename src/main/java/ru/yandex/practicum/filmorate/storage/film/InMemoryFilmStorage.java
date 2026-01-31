package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

@Component
public class InMemoryFilmStorage extends BaseStorage<Film> implements FilmStorage {

    @Override
    public Film create(Film data) {

        Film newFilm = super.add(data);

        logger.info("Фильм успешно добавлен: {}", newFilm);

        return newFilm;
    }

    @Override
    public Film update(Film data) {

        logger.info("Фильм успешно обновлен: {}", data);

        return dataList.put(data.getId(), data);
    }
}
