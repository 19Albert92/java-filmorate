package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface CrudMethodsStorage<T, K> {

    T findById(Long id);

    T create(T data);

    T update(T data);

    Collection<T> findAll();
}
