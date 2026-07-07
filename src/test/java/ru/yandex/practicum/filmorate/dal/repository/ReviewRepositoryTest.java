package ru.yandex.practicum.filmorate.dal.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.dal.ReviewVoteRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.ReviewRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.ReviewVoteRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@JdbcTest
@AutoConfigureTestDatabase
@Import({ReviewRepositoryImpl.class, ReviewRowMapper.class, UserRepositoryImpl.class, UserRowMapper.class,
        FilmRepositoryImpl.class, FilmRowMapper.class, ReviewVoteRepositoryImpl.class, ReviewVoteRowMapper.class})
@RequiredArgsConstructor(onConstructor_ =  @Autowired)
public class ReviewRepositoryTest {

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    private final FilmRepository filmRepository;

    private final ReviewVoteRepository voteRepository;

    private List<Film> films = new ArrayList<>();

    private List<Review> reviews = new ArrayList<>();

    private List<User> users = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        Mpa mpa = Mpa.builder().id(1).build();

        users = EntityGenerator.generateUsers(10).stream()
                .map(userRepository::create)
                .toList();

        films = EntityGenerator.generateFilms(10, mpa).stream()
                .map(filmRepository::create)
                .toList();

        reviews = EntityGenerator.generateReview(5, users, films).stream()
                .map(reviewRepository::create)
                .toList();
    }

    @Test
    public void should_find_review_by_id() {

        Long filmId = reviews.getFirst().getId();

        Optional<Review> finedFilm = reviewRepository.findById(filmId);

        AssertionsForClassTypes.assertThat(finedFilm)
                .isPresent()
                .isNotNull()
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .as("Должен вернуть отзыв с id " + filmId)
                                .hasFieldOrPropertyWithValue("id", filmId)
                );
    }

    @Test
    public void should_delete_review_by_id() {

        Long reviewId = reviews.getFirst().getId();

        reviewRepository.deleteReview(reviewId);

        int expectedCount = reviews.size() - 1;

        Assertions.assertFalse(reviewRepository.findById(reviewId).isPresent(),
                "Отзыв должен был удалится");

        Assertions.assertEquals(expectedCount, reviewRepository.findAll().size(),
                String.format("Должно было вернутся: %d", expectedCount));
    }

    @Test
    public void should_update_review_by_id() {

        Review review = reviews.getFirst();

        String expectedContent = "Update review content bla bla bla";

        boolean expectedIsPositive = !review.getIsPositive();

        review.setContent(expectedContent);

        review.setIsPositive(expectedIsPositive);

        Review updateReview = reviewRepository.update(review);

        AssertionsForClassTypes.assertThat(updateReview)
                .isNotNull()
                .as("Конетент отзыва должен был измениться на " + expectedContent)
                .hasFieldOrPropertyWithValue("content", expectedContent)
                .as("Тип отзыва должен был измениться на " + expectedIsPositive)
                .hasFieldOrPropertyWithValue("isPositive", expectedIsPositive);
    }

    @Test
    public void should_filter_reviews_by_film_id() {

        Long filmId = films.getFirst().getId();

        long expectedCount = reviews.stream()
                .filter(review -> review.getFilmId().equals(filmId))
                .count();

        int reviewCount = reviewRepository.findAll(filmId, 20).size();

        Assertions.assertEquals(expectedCount, reviewCount,
                String.format("Должно было вернуться %d записей", expectedCount));
    }

    @Test
    public void should_updated_review_useful() {

        int expectedCount = 5;

        Long filmId = reviews.getFirst().getId();

        reviewRepository.updateUseful(expectedCount,  filmId);

        Optional<Review> updatedReview = reviewRepository.findById(filmId);

        AssertionsForClassTypes.assertThat(updatedReview)
                .as("Должен вернуться измененный отзыв")
                .isPresent()
                .isNotNull()
                .hasValueSatisfying(review ->
                        AssertionsForClassTypes.assertThat(review)
                                .as("Рейтинг отзыва должен был измениться на " + expectedCount)
                                .hasFieldOrPropertyWithValue("useful", expectedCount)
                );

        int newExpectedCount = 4;

        reviewRepository.updateUseful(-1,  filmId);

        updatedReview = reviewRepository.findById(filmId);

        AssertionsForClassTypes.assertThat(updatedReview)
                .as("Должен вернуться измененный отзыв")
                .isPresent()
                .isNotNull()
                .hasValueSatisfying(review ->
                        AssertionsForClassTypes.assertThat(review)
                                .as("Рейтинг отзыва должен был измениться на " + newExpectedCount)
                                .hasFieldOrPropertyWithValue("useful", newExpectedCount)
                );
    }

    @Test
    public void should_throw_exception_when_adding_duplicate_vote() {

        Long userId = users.getFirst().getId();

        Long reviewId = reviews.getFirst().getId();

        voteRepository.insertVote(userId, reviewId, true);

        Collection<ReviewVote> votes = voteRepository.getVotes(reviewId);

        Assertions.assertEquals(1, votes.size(), "Новая запись не должна была добавиться");
    }
}
