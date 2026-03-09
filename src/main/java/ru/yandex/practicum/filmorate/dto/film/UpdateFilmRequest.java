package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.practicum.filmorate.util.validate.OnUpdate;

import java.time.LocalDate;

@Data
public class UpdateFilmRequest {

    @NotNull(groups = OnUpdate.class, message = "Id не пришел или пришел пустым")
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;

    public boolean hasName() {
        return name != null && !name.isEmpty();
    }

    public boolean hasDescription() {
        return description != null && !description.isEmpty();
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }
}
