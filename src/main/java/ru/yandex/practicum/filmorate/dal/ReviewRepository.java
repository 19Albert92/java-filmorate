package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewRepository extends CrudMethodsRepository<Review, Long> {

    Collection<Review> findAll(Long filmId, Integer count);

    Collection<Review> findAll(Integer count);

    boolean deleteReview(Long reviewId);

    Review create(Review review);

    Review update(Review review);

    void updateUseful(Integer useful, Long reviewId);
}