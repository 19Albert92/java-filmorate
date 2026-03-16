package ru.yandex.practicum.filmorate.dal;

import java.util.Collection;
import java.util.Optional;

public interface CrudMethodsRepository<T, K> {

    Optional<T> findById(K id);

    T create(T data);

    T update(T data);

    Collection<T> findAll();
}
