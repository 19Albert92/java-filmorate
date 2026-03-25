package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.DirectorRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dto.director.CreateDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository directorRepository;
    private final FilmRepository filmRepository;

    @Override
    public void saveDirectors(Long filmId, Set<Long> uniqDirectors) {
        List<Object[]> directors = uniqDirectors.stream().map(id -> new Object[]{filmId, id}).toList();

        directorRepository.save(directors);
    }

    @Override
    public DirectorDto findById(Long id) {
        return directorRepository.findById(id)
                .map(DirectorMapper::mapToDirectorDto)
                .orElseThrow(() -> new NotFoundException("Режиссер не найден"));
    }

    @Override
    public List<DirectorDto> findAll() {
        return directorRepository.findAll().stream()
                .map(DirectorMapper::mapToDirectorDto)
                .toList();
    }

    @Override
    public DirectorDto create(CreateDirectorRequest request) {
        Director director = DirectorMapper.mapToDirector(request);

        director = directorRepository.create(director);

        return DirectorMapper.mapToDirectorDto(director);
    }

    @Override
    public DirectorDto update(UpdateDirectorRequest request) {
        Director director = directorRepository.findById(request.getId())
                .map(u -> DirectorMapper.mapToUpdateFields(u, request))
                .orElseThrow(() -> new NotFoundException("Режиссер не найден"));

        director = directorRepository.update(director);

        return DirectorMapper.mapToDirectorDto(director);
    }

    @Override
    public boolean delete(Long id) {
        checkDirectorExists(id);

        return directorRepository.delete(id);
    }

    @Override
    public List<Director> findDirectorsByFilmId(Long id) {
        checkFilmExists(id);

        return directorRepository.findDirectorsByFilmId(id);
    }

    private void checkDirectorExists(Long directorId) {
        directorRepository.findById(directorId)
                .orElseThrow(() -> new NotFoundException("Режиссер не найден"));
    }

    private void checkFilmExists(Long filmId) {
        filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }
}
