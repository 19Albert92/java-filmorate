package ru.yandex.practicum.filmorate.dal.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.DirectorRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.LikeRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mapper.DirectorRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.LikeRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmRepositoryImpl.class, FilmRowMapper.class, LikeRepositoryImpl.class, LikeRowMapper.class,
        UserRepositoryImpl.class, UserRowMapper.class, DirectorRepositoryImpl.class, DirectorRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmRepositoryTest {

    private static final String USER_NAME = "TestName";
    private static final String USER_LOGIN = "TestLogin";
    private static final String USER_EMAIL = "test1@test.com";
    private static final LocalDate USER_BIRTHDAY = LocalDate.of(1990, 5, 5);

    private static final String FRIEND_NAME = "FriendName";
    private static final String FRIEND_LOGIN = "FriendLogin";
    private static final String FRIEND_EMAIL = "friend@test.com";
    private static final LocalDate FRIEND_BIRTHDAY = LocalDate.of(1997, 3, 23);

    private static final String ANOTHER_USER_NAME = "AnotherName";
    private static final String ANOTHER_USER_LOGIN = "AnotherLogin";
    private static final String ANOTHER_USER_EMAIL = "another@test.com";
    private static final LocalDate ANOTHER_USER_BIRTHDAY = LocalDate.of(1986, 11, 14);

    private final FilmRepository filmRepository;

    private final LikeRepository likeRepository;

    private final UserRepository userRepository;

    private final DirectorRepository directorRepository;

    private final List<Film> films = new ArrayList<>();

    private User user;

    private Director director;

    @BeforeEach
    public void setUp() {

        Mpa mpa = Mpa.builder().id(1).build();

        user = User.builder()
                .name(USER_NAME)
                .login(USER_LOGIN)
                .email(USER_EMAIL)
                .birthday(USER_BIRTHDAY)
                .build();

        user = userRepository.create(user);

        List<Film> newFilms = EntityGenerator.generateFilms(6, mpa);

        newFilms.forEach(film -> films.add(filmRepository.create(film)));
    }

    @Test
    public void should_find_film_by_id() {

        Long filmId = films.getFirst().getId();

        Optional<Film> finedFilm = filmRepository.findById(filmId);

        AssertionsForClassTypes.assertThat(finedFilm)
                .isPresent()
                .isNotNull()
                .hasValueSatisfying(film ->
                        AssertionsForClassTypes.assertThat(film)
                                .as("Должен вернуть фильм в id " + filmId)
                                .hasFieldOrPropertyWithValue("id", filmId)
                );

    }

    @Test
    public void should_find_all_films() {

        Collection<Film> finedFilms = filmRepository.findAll();

        Assertions.assertThat(finedFilms)
                .isNotNull()
                .as("Должно вернутся 2 фильма", films.size())
                .hasSize(films.size());
    }

    @Test
    public void should_find_and_update_film_by_id() {

        String newName = "New test film_2 name";
        String newDescription = "New test film_2 description";
        Film film = films.getFirst();
        long filmId = film.getId();

        film.setName(newName);
        film.setDescription(newDescription);

        filmRepository.update(film);

        Optional<Film> updatedInDb = filmRepository.findById(filmId);

        AssertionsForClassTypes.assertThat(updatedInDb)
                .as("Фильм с id %d должен быть после обновления", filmId)
                .isPresent()
                .get()
                .satisfies(f -> {
                    AssertionsForClassTypes.assertThat(f.getName())
                            .as("Имя не совпадает с новым").isEqualTo(newName);
                    AssertionsForClassTypes.assertThat(f.getDescription())
                            .as("Описание не совпадает с новым").isEqualTo(newDescription);
                });
    }

    @Test
    public void should_best_popular_film_by_limit() {

        Film expectedPopularFilm = films.getLast();

        Like like = Like.builder()
                .userId(user.getId())
                .filmId(expectedPopularFilm.getId())
                .build();

        likeRepository.addLike(like.getFilmId(), like.getUserId());

        List<Film> finedFilms = filmRepository.getPopularFilmByLikes(4);

        Assertions.assertThat(finedFilms)
                .isNotNull()
                .as("Количество фильмов должно равнятся %d", 4)
                .hasSize(4)
                .first()
                .as("Первый должен быть с наибольшим лайком")
                .hasFieldOrPropertyWithValue("id", expectedPopularFilm.getId())
                .hasFieldOrPropertyWithValue("name", expectedPopularFilm.getName());
    }

    @Test
    public void should_find_common_films_sorted_by_popularity() {
        User friend = User.builder()
                .name(FRIEND_NAME)
                .login(FRIEND_LOGIN)
                .email(FRIEND_EMAIL)
                .birthday(FRIEND_BIRTHDAY)
                .build();

        User extraUser = User.builder()
                .name(ANOTHER_USER_NAME)
                .login(ANOTHER_USER_LOGIN)
                .email(ANOTHER_USER_EMAIL)
                .birthday(ANOTHER_USER_BIRTHDAY)
                .build();

        friend = userRepository.create(friend);
        User anotherUser = userRepository.create(extraUser);

        Film morePopularCommon = films.getFirst();
        Film lessPopularCommon = films.get(1);

        likeRepository.addLike(morePopularCommon.getId(), user.getId());
        likeRepository.addLike(morePopularCommon.getId(), friend.getId());
        likeRepository.addLike(morePopularCommon.getId(), anotherUser.getId());

        likeRepository.addLike(lessPopularCommon.getId(), user.getId());
        likeRepository.addLike(lessPopularCommon.getId(), friend.getId());

        List<Film> commonFilms = filmRepository.getCommonFilms(user.getId(), friend.getId());

        Assertions.assertThat(commonFilms)
                .isNotNull()
                .as("Должны вернуться общие фильмы")
                .hasSize(2)
                .first()
                .as("Первым должен быть более популярный фильм по лайкам")
                .hasFieldOrPropertyWithValue("id", morePopularCommon.getId())
                .hasFieldOrPropertyWithValue("name", morePopularCommon.getName());
    }

    @Test
    public void should_return_sorted_by_likes_films_by_director() {
        director = Director.builder()
                .name("TestName")
                .build();

        director = directorRepository.create(director);

        Long directorId = director.getId();

        Mpa mpa = Mpa.builder().id(1).build();

        Film firstFilm = Film.builder()
                .name("First Film")
                .description("description")
                .releaseDate(LocalDate.of(2000, 4, 1))
                .duration(120L)
                .mpa(mpa)
                .build();

        Film secondFilm = Film.builder()
                .name("Second Film")
                .description("description")
                .releaseDate(LocalDate.of(1990, 4, 1))
                .duration(120L)
                .mpa(mpa)
                .build();

        Film thirdFilm = Film.builder()
                .name("Third Film")
                .description("description")
                .releaseDate(LocalDate.of(2005, 4, 1))
                .duration(120L)
                .mpa(mpa)
                .build();

        List<Film> newFilms = List.of(firstFilm, secondFilm, thirdFilm);

        newFilms.forEach(film -> filmRepository.create(film));

        newFilms.forEach(film -> directorRepository.save(Collections.singletonList(new Object[]{film.getId(), directorId})));

        newFilms.forEach(film -> film.setDirectors(List.of(director)));

        Like like = Like.builder()
                .userId(user.getId())
                .filmId(secondFilm.getId())
                .build();

        likeRepository.addLike(like.getFilmId(), like.getUserId());

        List<Film> foundFilms = filmRepository.getFilmsByDirectorIdSortedByLikes(directorId);

        Assertions.assertThat(foundFilms)
                .isNotNull()
                .as("Количество фильмов должно быть %d", 3)
                .hasSize(3)
                .first()
                .as("Первый фильм должен быть с наибольшим количеством лайков")
                .hasFieldOrPropertyWithValue("id", secondFilm.getId())
                .hasFieldOrPropertyWithValue("name", secondFilm.getName());
    }

    @Test
    public void should_return_sorted_by_year_films_by_director() {
        director = Director.builder()
                .name("TestName")
                .build();

        director = directorRepository.create(director);

        Long directorId = director.getId();

        Mpa mpa = Mpa.builder().id(1).build();

        Film firstFilm = Film.builder()
                .name("First Film")
                .description("description")
                .releaseDate(LocalDate.of(2000, 4, 1))
                .duration(120L)
                .mpa(mpa)
                .build();

        Film secondFilm = Film.builder()
                .name("Second Film")
                .description("description")
                .releaseDate(LocalDate.of(1990, 4, 1))
                .duration(120L)
                .mpa(mpa)
                .build();

        Film thirdFilm = Film.builder()
                .name("Third Film")
                .description("description")
                .releaseDate(LocalDate.of(2005, 4, 1))
                .duration(120L)
                .mpa(mpa)
                .build();

        List<Film> newFilms = List.of(firstFilm, secondFilm, thirdFilm);

        newFilms.forEach(film -> filmRepository.create(film));

        newFilms.forEach(film -> directorRepository.save(Collections.singletonList(new Object[]{film.getId(), directorId})));

        newFilms.forEach(film -> film.setDirectors(List.of(director)));

        List<Film> foundFilms = filmRepository.getFilmsByDirectorIdSortedByYear(directorId);

        Assertions.assertThat(foundFilms)
                .isNotNull()
                .as("Количество фильмов должно быть %d", 3)
                .hasSize(3)
                .as("Фильмы должны быть отсортированы по возрастанию года")
                .extracting(Film::getReleaseDate)
                .isSorted();
    }
}
