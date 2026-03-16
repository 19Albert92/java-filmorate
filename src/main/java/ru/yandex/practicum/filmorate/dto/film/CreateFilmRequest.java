package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.util.validate.MinDateValidator;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateFilmRequest {
    @NotNull(message = "поле name не пришел")
    @NotBlank(message = "name не может быть пустым")
    private String name;
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;
    @MinDateValidator(message = "дата релиза должна быть не раньше 28 декабря 1895 года")
    @NotNull(message = "поле releaseDate не пришел")
    private LocalDate releaseDate;
    @NotNull(message = "поле duration не пришел")
    @Positive(message = "Продолжительность фильма должна быть больше 0")
    private Long duration;
    private List<Genre> genres;
    private Mpa mpa;
}
