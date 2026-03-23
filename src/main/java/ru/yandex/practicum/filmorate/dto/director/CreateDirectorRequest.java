package ru.yandex.practicum.filmorate.dto.director;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDirectorRequest {
    @NotBlank(message = "поле name не может быть пустым")
    private String name;
}
