package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.director.CreateDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.model.Director;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DirectorMapper {
    public static DirectorDto mapToDirectorDto(Director director) {
        DirectorDto directorDto = new DirectorDto();
        directorDto.setId(director.getId());
        directorDto.setName(director.getName());

        return directorDto;
    }

    public static Director mapToDirector(CreateDirectorRequest request) {
        return Director.builder()
                .name(request.getName())
                .build();
    }

    public static Director mapToUpdateFields(Director director, UpdateDirectorRequest request) {
        if (request.hasName()) {
            director.setName(request.getName());
        }

        return director;
    }
}
