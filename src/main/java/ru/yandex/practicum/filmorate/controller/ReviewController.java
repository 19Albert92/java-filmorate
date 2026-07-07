package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.review.CreateReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.dto.review.ReviewVoteRequest;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.util.validate.CommonValidate;

import java.util.Collection;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto create(@RequestBody @Valid CreateReviewRequest reviewRequest) {
        return reviewService.createReview(reviewRequest);
    }

    @PutMapping
    public ReviewDto update(@RequestBody @Valid UpdateReviewRequest reviewRequest) {
        return reviewService.updateReview(reviewRequest);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");
        return reviewService.deleteReviewById(id);
    }

    @GetMapping("/{id}")
    public ReviewDto get(@PathVariable Long id) {
        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");
        return reviewService.findReviewById(id);
    }

    @GetMapping
    public Collection<ReviewDto> getAll(
            @RequestParam(required = false) Long filmId,
            @RequestParam(defaultValue = "10") Integer count
    ) {
        return reviewService.findReviewsByFilmId(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void saveLike(@PathVariable Long id, @PathVariable Long userId) {
        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");
        CommonValidate.checkNotNullAndPositive(userId, "Параметр userid должен быть положительным");

        reviewService.setVoteState(new ReviewVoteRequest(id, userId, true, true));
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void saveDislike(@PathVariable Long id, @PathVariable Long userId) {
        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");
        CommonValidate.checkNotNullAndPositive(userId, "Параметр userid должен быть положительным");

        reviewService.setVoteState(new ReviewVoteRequest(id, userId, false, true));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");
        CommonValidate.checkNotNullAndPositive(userId, "Параметр userid должен быть положительным");

        reviewService.setVoteState(new ReviewVoteRequest(id, userId, true, false));
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable Long id, @PathVariable Long userId) {
        CommonValidate.checkNotNullAndPositive(id, "Параметр id должен быть положительным");
        CommonValidate.checkNotNullAndPositive(userId, "Параметр userid должен быть положительным");

        reviewService.setVoteState(new ReviewVoteRequest(id, userId, false, false));
    }
}
