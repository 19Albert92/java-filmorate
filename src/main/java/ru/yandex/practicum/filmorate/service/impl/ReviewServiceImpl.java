package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.dto.review.CreateReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.ReviewVoteRequest;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.ReviewVoteService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Service
@Log4j2
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserService userService;

    private final FilmService filmService;

    private final ReviewVoteService voteService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserService userService, FilmService filmService, ReviewVoteService voteService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.filmService = filmService;
        this.voteService = voteService;
    }

    @Override
    public ReviewDto createReview(CreateReviewRequest reviewRequest) {

        userService.findById(reviewRequest.getUserId());

        filmService.findById(reviewRequest.getFilmId());

        Review review = ReviewMapper.mapToReview(reviewRequest);

        review = reviewRepository.create(review);

        return ReviewMapper.mapToReviewDto(review);
    }

    @Override
    public ReviewDto updateReview(UpdateReviewRequest reviewRequest) {

        Review review = reviewRepository.findById(reviewRequest.getReviewId())
                .orElseThrow(() -> new NotFoundException("Отзыва с данным id нет"));

        Review returningReview = ReviewMapper.mapToUpdateFields(review, reviewRequest);

        return ReviewMapper.mapToReviewDto(reviewRepository.update(returningReview));
    }

    @Override
    public ReviewDto findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .map(ReviewMapper::mapToReviewDto)
                .orElseThrow(() -> new NotFoundException("Отзыв не найден"));
    }

    @Override
    public boolean deleteReviewById(Long reviewId) {

        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыва с данным id нет"));

        return reviewRepository.deleteReview(reviewId);
    }

    @Override
    public Collection<ReviewDto> findReviewsByFilmId(Long filmId, Integer count) {

        if (filmId == null) {
            return reviewRepository.findAll(count).stream()
                    .map(ReviewMapper::mapToReviewDto)
                    .toList();
        }

        return reviewRepository.findAll(filmId, count).stream()
                .map(ReviewMapper::mapToReviewDto)
                .toList();
    }

    @Override
    @Transactional
    public void setVoteState(ReviewVoteRequest request) {

        checkReviewAndUser(request);

        if (request.isAdding()) {
            addStateLikeOrDislike(request);
        } else {
            deleteStateLikeOrDislike(request);
        }
    }

    private void addStateLikeOrDislike(ReviewVoteRequest request) {

        int delta;

        if (voteService.hasVoteState(request)) {

            voteService.addVoteState(request);

            delta = request.getIsLike() ? 1 : -1;
        } else {
            voteService.addVoteState(request);

            delta = request.getIsLike() ? 2 : -2;
        }

        reviewRepository.updateUseful(delta, request.getReviewId());
    }

    private void deleteStateLikeOrDislike(ReviewVoteRequest request) {

        boolean deleted = voteService.removeVoteState(request);

        if (deleted) {

            int delta = request.getIsLike() ? -1 : 1;

            reviewRepository.updateUseful(delta, request.getReviewId());
        }
    }

    void checkReviewAndUser(ReviewVoteRequest reviewVoteRequest) {
        userService.findById(reviewVoteRequest.getUserId());
        reviewRepository.findById(reviewVoteRequest.getReviewId());
    }
}
