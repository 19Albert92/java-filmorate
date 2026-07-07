package ru.yandex.practicum.filmorate.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewVoteRequest {
    @NotNull(message = "Идентификатор отзыва обязателен")
    private Long reviewId;
    @NotNull(message = "Идентификатор пользователя обязателен")
    private Long userId;
    @JsonProperty("isLike")
    private Boolean isLike;
    private boolean isAdding;
}
