package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.utill.Identifiable;
import ru.yandex.practicum.filmorate.model.utill.OnUpdate;

import java.time.LocalDate;

@Data
public class Film implements Identifiable {
    @NotNull(groups = OnUpdate.class, message = "Id не пришел или пришел пустым")
    private Long id;
    @NotNull
    @NotBlank(message = "name не может быть пустым")
    private String name;
    @Size(max = 200, message = "максимальная длина description — 200 символов")
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть больше 0")
    private Long duration;
}
