package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.NotFoundDateException;
import ru.yandex.practicum.filmorate.model.Identifiable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseStorage<T extends Identifiable> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    protected final Map<Long, T> dataList = new HashMap<>();

    protected Long getNextId() {
        long currentMaxId = dataList.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Collection<T> findAll() {
        return dataList.values();
    }

    public T findById(Long id) throws NotFoundDateException {

        if (!dataList.containsKey(id)) {
            throw new NotFoundDateException(String.format("Пользователь с id: %d не был найден", id));
        }

        return dataList.get(id);
    }

    protected T add(T obj) {
        long generatedId = getNextId();
        obj.setId(generatedId);
        return dataList.put(generatedId, obj);
    }
}
