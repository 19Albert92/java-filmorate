package ru.yandex.practicum.filmorate.dal.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FeedsRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mapper.*;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@JdbcTest
@AutoConfigureTestDatabase
@Import({ReviewRepositoryImpl.class, ReviewRowMapper.class,
        FilmRepositoryImpl.class, FilmRowMapper.class,
        UserRepositoryImpl.class, UserRowMapper.class,
        FeedsRepositoryImpl.class, FeedRowMapper.class,
        ReviewVoteRepositoryImpl.class, ReviewVoteRowMapper.class})
@RequiredArgsConstructor(onConstructor_ =  @Autowired)
public class FeedsRepositoryTest {

    private final ReviewRepository reviewRepository;

    private final FilmRepository filmRepository;

    private final FeedsRepository feedRepository;

    private final UserRepository userRepository;

    private Supplier<Long> getTimestamp() {
        return () -> Timestamp.from(Instant.now()).getTime();
    }

    @Test
    public void should_return_7_events() {

        int expectedCount = 7;

        List<User> users = EntityGenerator.generateUsers(2).stream()
                .map(userRepository::create)
                .toList();

        Mpa mpa = Mpa.builder().id(1).build();

        List<Film> films = EntityGenerator.generateFilms(2, mpa).stream()
                .map(filmRepository::create)
                .toList();

        List<Review> reviews = EntityGenerator.generateReview(1, users, films).stream()
                .map(reviewRepository::create)
                .toList();

        User firstUser = users.getFirst();

        User lastUser = users.getLast();

        Film firstFilm = films.getFirst();

        Review firstReview = reviews.getFirst();

        List<Feed> feeds = List.of(
                Feed.builder().userId(firstUser.getId())
                        .eventType(EventType.FRIEND.name())
                        .operation(OperationType.ADD.name())
                        .entityId(lastUser.getId())
                        .timestamp(getTimestamp().get())
                        .build(),
                Feed.builder()
                        .userId(firstUser.getId())
                        .eventType(EventType.FRIEND.name())
                        .operation(OperationType.REMOVE.name())
                        .entityId(lastUser.getId())
                        .timestamp(getTimestamp().get())
                        .build(),
                Feed.builder()
                        .userId(firstUser.getId())
                        .eventType(EventType.LIKE.name())
                        .operation(OperationType.ADD.name())
                        .entityId(firstFilm.getId())
                        .timestamp(getTimestamp().get())
                        .build(),
                Feed.builder()
                        .userId(firstUser.getId())
                        .eventType(EventType.LIKE.name())
                        .operation(OperationType.REMOVE.name())
                        .entityId(firstFilm.getId())
                        .timestamp(getTimestamp().get())
                        .build(),
                Feed.builder()
                        .userId(firstUser.getId())
                        .eventType(EventType.REVIEW.name())
                        .operation(OperationType.ADD.name())
                        .entityId(firstReview.getId())
                        .timestamp(getTimestamp().get())
                        .build(),
                Feed.builder()
                        .userId(firstUser.getId())
                        .eventType(EventType.REVIEW.name())
                        .operation(OperationType.REMOVE.name())
                        .entityId(firstReview.getId())
                        .timestamp(getTimestamp().get())
                        .build(),
                Feed.builder()
                        .userId(firstUser.getId())
                        .eventType(EventType.REVIEW.name())
                        .operation(OperationType.UPDATE.name())
                        .entityId(firstReview.getId())
                        .timestamp(getTimestamp().get())
                        .build()
        );

        feeds.forEach(feedRepository::addFeed);

        Collection<Feed> allFeeds = feedRepository.getFeeds(firstUser.getId());

        Assertions.assertEquals(expectedCount, allFeeds.size(),
                String.format("Доджно было вернуться %d записей", expectedCount));

        org.assertj.core.api.Assertions.assertThat(allFeeds)
                .first()
                .isNotNull()
                .as("Идентификатор сущности должен был быть %d", lastUser.getId())
                .hasFieldOrPropertyWithValue("entityId", lastUser.getId())
                .as("Название события должно было быть %s", EventType.FRIEND.name())
                .hasFieldOrPropertyWithValue("eventType", EventType.FRIEND.name());
    }
}
