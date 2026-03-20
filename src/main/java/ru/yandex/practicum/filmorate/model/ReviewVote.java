package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewVote {
    private Long reviewId;
    private Long userId;
    private boolean voteType;
}
