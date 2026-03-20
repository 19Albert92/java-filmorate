package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.review.CreateReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.ReviewVoteRequest;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;

import java.util.Collection;

public interface ReviewService {

    ReviewDto createReview(CreateReviewRequest reviewRequest);

    ReviewDto updateReview(UpdateReviewRequest reviewRequest);

    ReviewDto findReviewById(Long reviewId);

    boolean deleteReviewById(Long reviewId);

    Collection<ReviewDto> findReviewsByFilmId(Long filmId, Integer count);

    void setVoteState(ReviewVoteRequest reviewVoteRequest);
}
