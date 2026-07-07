package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.review.CreateReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.model.Review;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewMapper {

    private static final int EMPTY_USEFUL = 0;

    public static ReviewDto mapToReviewDto(Review review) {

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewId(review.getId());
        reviewDto.setFilmId(review.getFilmId());
        reviewDto.setUserId(review.getUserId());
        reviewDto.setContent(review.getContent());
        reviewDto.setUseful(review.getUseful());
        reviewDto.setPositive(review.getIsPositive());

        return reviewDto;
    }

    public static Review mapToReview(CreateReviewRequest reviewRequest) {
        return Review.builder()
                .userId(reviewRequest.getUserId())
                .content(reviewRequest.getContent())
                .filmId(reviewRequest.getFilmId())
                .isPositive(reviewRequest.getIsPositive())
                .useful(EMPTY_USEFUL)
                .build();
    }

    public static Review mapToUpdateFields(Review review, UpdateReviewRequest reviewRequest) {

        if (reviewRequest.hasContent()) {
            review.setContent(reviewRequest.getContent());
        }

        if (reviewRequest.hasIsPositive()) {
            review.setIsPositive(reviewRequest.getIsPositive());
        }

        return review;
    }
}
