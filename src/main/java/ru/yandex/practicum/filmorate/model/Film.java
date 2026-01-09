package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.util.Identifiable;
import ru.yandex.practicum.filmorate.util.OnUpdate;
import ru.yandex.practicum.filmorate.util.validate.MinDateValidator;

import java.time.LocalDate;

@Data
@Builder
public class Film implements Identifiable {
    @NotNull(groups = OnUpdate.class, message = "id не пришел")
    private Long id;
    @NotNull(message = "name не пришел")
    @NotBlank(message = "name не может быть пустым")
    private String name;
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;
    @MinDateValidator(message = "дата релиза должна быть не раньше 28 декабря 1895 года")
    @NotNull(message = "releaseDate не пришел")
    private LocalDate releaseDate;
    @NotNull(message = "duration не пришел")
    @Positive(message = "Продолжительность фильма должна быть больше 0")
    private Long duration;
}
