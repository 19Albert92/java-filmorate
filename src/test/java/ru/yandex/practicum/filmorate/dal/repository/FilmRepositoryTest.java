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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmRepositoryImpl.class, FilmRowMapper.class, LikeRepositoryImpl.class, LikeRowMapper.class,
        UserRepositoryImpl.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ =  @Autowired)
public class FilmRepositoryTest {

    private final FilmRepository filmRepository;

    private final LikeRepository likeRepository;

    private final UserRepository userRepository;

    private final List<Film> films = new ArrayList<>();

    private User user;

    @BeforeEach
    public void setUp() {

        Mpa mpa = Mpa.builder().id(1).build();

        user = User.builder()
                .name("TestName")
                .login("TestLogin")
                .email("test1@test.com")
                .birthday(LocalDate.of(1998, 1, 1))
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
}
