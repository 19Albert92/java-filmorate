import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
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
import ru.yandex.practicum.filmorate.dal.repository.EntityGenerator;
import ru.yandex.practicum.filmorate.dal.repository.FilmRepositoryImpl;
import ru.yandex.practicum.filmorate.dal.repository.LikeRepositoryImpl;
import ru.yandex.practicum.filmorate.dal.repository.UserRepositoryImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.SearchBy;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmRepositoryImpl.class, FilmRowMapper.class, LikeRepositoryImpl.class, LikeRowMapper.class,
        UserRepositoryImpl.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SearchFilmRequestTest {

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

        List<Film> newFilms = EntityGenerator.generateFilms(3, mpa);

        newFilms.forEach(film -> films.add(filmRepository.create(film)));
    }

    @Test
    public void getFilteredFilms_whenNoParams_shouldReturnFilmsSortedByPopularity() {

        Film expectedPopularFilm = films.getLast();

        Like like = Like.builder()
                .userId(user.getId())
                .filmId(expectedPopularFilm.getId())
                .build();

        likeRepository.addLike(like.getFilmId(), like.getUserId());

        List<Film> foundFilms = filmRepository.getPopularFilmByLikes();

        Assertions.assertThat(foundFilms)
                .isNotNull()
                .as("Количество фильмов должно быть %d", 3)
                .hasSize(3)
                .first()
                .as("Первый должен быть с наибольшим количеством лайков")
                .hasFieldOrPropertyWithValue("id", expectedPopularFilm.getId())
                .hasFieldOrPropertyWithValue("name", expectedPopularFilm.getName());
    }

    @Test
    public void getFilteredFilms_whenAllQueryParams_shouldReturnFilmsWithTitleFromParam() {

        Mpa mpa = Mpa.builder().id(1).build();

        Film firstFilm = Film.builder()
                .name("Best Film Ever")
                .description("description")
                .releaseDate(LocalDate.of(1990, 4, 1))
                .duration(120L)
                .mpa(mpa)
                .build();

        Film secondFilm = Film.builder()
                .name("The Best Film Ever")
                .description("description")
                .releaseDate(LocalDate.of(1990, 4, 1))
                .duration(120L)
                .mpa(mpa)
                .build();

        Film thirdFilm = Film.builder()
                .name("Just Film")
                .description("description")
                .releaseDate(LocalDate.of(1990, 4, 1))
                .duration(120L)
                .mpa(mpa)
                .build();

        List<Film> newFilms = List.of(firstFilm, secondFilm, thirdFilm);

        newFilms.forEach(film -> filmRepository.create(film));

        List<Film> foundFilms = filmRepository.getFilteredFilms("best", List.of(SearchBy.title.name()));

        Assertions.assertThat(foundFilms)
                .isNotNull()
                .as("Количество фильмов должно быть %d", 2)
                .hasSize(2)
                .as("Названия фильмов должны соответствовать запросу")
                .allMatch(film -> film.getName().toLowerCase().contains("best"));
    }

}
