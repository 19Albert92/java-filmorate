package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.director.CreateDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Set;

public interface DirectorService {
    void saveDirectors(Long filmId, Set<Long> directors);

    DirectorDto findById(Long id);

    List<DirectorDto> findAll();

    DirectorDto create(CreateDirectorRequest request);

    DirectorDto update(UpdateDirectorRequest request);

    boolean delete(Long id);

    List<Director> findDirectorsByFilmId(Long id);
}
