package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.dto.feed.FeedDto;
import ru.yandex.practicum.filmorate.dto.review.CreateReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.ReviewVoteRequest;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.*;

import java.util.Collection;

@Service
@Log4j2
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserService userService;

    private final FeedService feedService;

    private final FilmService filmService;

    private final ReviewVoteService voteService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserService userService, FeedService feedService, FilmService filmService, ReviewVoteService voteService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.feedService = feedService;
        this.filmService = filmService;
        this.voteService = voteService;
    }

    @Override
    public ReviewDto createReview(CreateReviewRequest reviewRequest) {

        userService.findById(reviewRequest.getUserId());

        filmService.findById(reviewRequest.getFilmId());

        Review review = ReviewMapper.mapToReview(reviewRequest);

        review = reviewRepository.create(review);

        saveFeed(review.getId(), review.getUserId(), OperationType.ADD);

        return ReviewMapper.mapToReviewDto(review);
    }

    @Override
    public ReviewDto updateReview(UpdateReviewRequest reviewRequest) {

        Review review = reviewRepository.findById(reviewRequest.getReviewId())
                .orElseThrow(() -> new NotFoundException("Отзыва с данным id нет"));

        Review returningReview = ReviewMapper.mapToUpdateFields(review, reviewRequest);

        saveFeed(reviewRequest.getReviewId(), review.getUserId(), OperationType.UPDATE);

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

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыва с данным id нет"));

        saveFeed(reviewId, review.getUserId(), OperationType.REMOVE);

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

    private void saveFeed(Long reviewId, Long userId, OperationType operation) {
        FeedDto feedDto = new FeedDto();
        feedDto.setEventType(EventType.REVIEW);
        feedDto.setEntityId(reviewId);
        feedDto.setUserId(userId);
        feedDto.setOperation(operation);
        feedService.addFeed(feedDto);
    }
}
