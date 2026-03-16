package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.dal.MpaRepository;

import java.util.List;

@Service
public class MpaServiceImpl implements MpaService {

    private final MpaRepository mpaStorage;

    public MpaServiceImpl(MpaRepository mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Mpa findById(int mpaId) {
        return mpaStorage.findById(mpaId)
                .orElseThrow(() -> new NotFoundException("Такого рейтинга нет"));
    }

    @Override
    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }
}
