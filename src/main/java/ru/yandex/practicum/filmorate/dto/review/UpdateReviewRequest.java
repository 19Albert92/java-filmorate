package ru.yandex.practicum.filmorate.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateReviewRequest {
    @NotNull(message = "Идентификатор отзыва обязателен")
    @Positive(message = "Идентификатор отзыва не должен быть мень нуля")
    private Long reviewId;
    @NotNull(message = "Текст отзыва обязателен")
    @NotBlank(message = "Текст отзыва не должен быть пустым")
    private String content;
    @NotNull(message = "Тип отзыва обязателен")
    @JsonProperty("isPositive")
    private Boolean isPositive;

    public boolean hasContent() {
        return content != null && !content.isEmpty();
    }

    public boolean hasIsPositive() {
        return isPositive != null;
    }
}
