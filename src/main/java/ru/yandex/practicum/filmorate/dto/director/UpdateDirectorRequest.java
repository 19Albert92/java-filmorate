package ru.yandex.practicum.filmorate.dto.director;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDirectorRequest {
    @NotNull(message = "id не может быть пустым")
    private Long id;

    @NotBlank(message = "поле name не может быть пустым")
    private String name;

    public boolean hasName() {
        return name != null && !name.isEmpty();
    }
}
