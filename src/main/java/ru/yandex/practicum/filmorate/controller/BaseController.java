package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.model.utill.Identifiable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseController<T extends Identifiable> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    protected final Map<Long, T> data = new HashMap<>();

    protected Long getNextId() {
        long currentMaxId = data.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    protected Collection<T> findAll() {
        return data.values();
    }

    protected T findById(Long id) {
        return data.get(id);
    }

    protected Long add(T obj) {
        long generatedId = getNextId();
        data.put(generatedId, obj);
        return generatedId;
    }
}
