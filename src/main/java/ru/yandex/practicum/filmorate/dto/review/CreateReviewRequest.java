package ru.yandex.practicum.filmorate.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateReviewRequest {
    @NotNull(message = "Текст отзыва обязателен")
    @NotBlank(message = "Текст отзыва не должен быть пустым")
    private String content;
    @NotNull(message = "Тип отзыва обязателен")
    @JsonProperty("isPositive")
    private Boolean isPositive;
    @NotNull(message = "Идентификатор пользователя обязателен")
    private Long userId;
    @NotNull(message = "Идентификатор фильма обязателен")
    private Long filmId;
}
