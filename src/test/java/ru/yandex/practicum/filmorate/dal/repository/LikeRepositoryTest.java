package ru.yandex.practicum.filmorate.dal.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.LikeRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.LikeRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmRepositoryImpl.class, FilmRowMapper.class,
        LikeRepositoryImpl.class, LikeRowMapper.class,
        UserRepositoryImpl.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ =  @Autowired)
public class LikeRepositoryTest {

    private final FilmRepository filmRepository;

    private final UserRepository userRepository;

    private final LikeRepository likeRepository;

    private final List<Film> films = new ArrayList<>();

    private final List<User> users = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        Mpa mpa = Mpa.builder().id(1).build();

        List<User> newUsers = EntityGenerator.generateUsers(6);

        newUsers.forEach(u -> users.add(userRepository.create(u)));

        List<Film> newFilms = EntityGenerator.generateFilms(4, mpa);

        newFilms.forEach(film -> films.add(filmRepository.create(film)));

        List<Like> newLike = List.of(
                Like.builder()
                        .userId(users.get(0).getId())
                        .filmId(films.get(0).getId())
                        .build(),
                Like.builder()
                        .userId(users.get(2).getId())
                        .filmId(films.get(1).getId())
                        .build(),
                Like.builder()
                        .userId(users.get(3).getId())
                        .filmId(films.get(0).getId())
                        .build()
        );

        newLike.forEach(like -> likeRepository.addLike(like.getFilmId(), like.getUserId()));
    }

    @Test
    public void should_confirm_user_liked_film() {

        Film film = films.getFirst();

        User user = users.getFirst();

        boolean hasLikeToFilm = likeRepository.isLikeExists(film.getId(), user.getId());

        AssertionsForClassTypes.assertThat(hasLikeToFilm)
                .as("Лайк был поставлен пользователем")
                .isTrue();

        film = films.get(1);

        user = users.get(3);

        hasLikeToFilm = likeRepository.isLikeExists(film.getId(), user.getId());

        AssertionsForClassTypes.assertThat(hasLikeToFilm)
                .as("Лайк не был поставлен пользователем")
                .isFalse();
    }

    @Test
    public void should_add_like_to_film() {

        Film film = films.getFirst();

        User user = users.get(1);

        boolean hasLikeToFilm = likeRepository.isLikeExists(film.getId(), user.getId());

        AssertionsForClassTypes.assertThat(hasLikeToFilm)
                .as("Лайк не был поставлен пользователем")
                .isFalse();

        likeRepository.addLike(film.getId(), user.getId());

        hasLikeToFilm = likeRepository.isLikeExists(film.getId(), user.getId());

        AssertionsForClassTypes.assertThat(hasLikeToFilm)
                .as("Лайк был поставлен пользователем")
                .isTrue();
    }

    @Test
    public void should_remove_like_to_film() {

        Film film = films.getFirst();

        User user = users.get(3);

        boolean hasLikeToFilm = likeRepository.isLikeExists(film.getId(), user.getId());

        AssertionsForClassTypes.assertThat(hasLikeToFilm)
                .as("Лайк был поставлен пользователем")
                .isTrue();

        likeRepository.deleteLike(film.getId(), user.getId());

        hasLikeToFilm = likeRepository.isLikeExists(film.getId(), user.getId());

        AssertionsForClassTypes.assertThat(hasLikeToFilm)
                .as("Лайк был удален")
                .isFalse();
    }
}
