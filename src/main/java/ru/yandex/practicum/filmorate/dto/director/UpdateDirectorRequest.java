package ru.yandex.practicum.filmorate.dto.director;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.practicum.filmorate.util.validate.OnUpdate;

@Data
public class UpdateDirectorRequest {
    @NotNull(groups = OnUpdate.class, message = "id не может быть пустым")
    private Long id;

    private String name;

    public boolean hasName() {
        return name != null && !name.isEmpty();
    }
}
