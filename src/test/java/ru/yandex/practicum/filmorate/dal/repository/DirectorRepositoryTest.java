package ru.yandex.practicum.filmorate.dal.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.DirectorRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.mapper.DirectorRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.tuple;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmRepositoryImpl.class, FilmRowMapper.class, DirectorRepositoryImpl.class, DirectorRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorRepositoryTest {
    private final FilmRepository filmRepository;
    private final DirectorRepository directorRepository;

    private final List<Film> films = new ArrayList<>();
    private Director director;

    @BeforeEach
    public void setUp() {
        Mpa mpa = Mpa.builder().id(1).build();

        director = Director.builder()
                .name("TestName")
                .build();

        director = directorRepository.create(director);

        List<Film> newFilms = EntityGenerator.generateFilms(6, mpa);

        newFilms.forEach(film -> films.add(filmRepository.create(film)));
    }

    @Test
    public void getDirectorById_shouldReturnDirector() {
        Long directorId = director.getId();

        Optional<Director> foundDirector = directorRepository.findById(directorId);

        Assertions.assertThat(foundDirector)
                .hasValueSatisfying(director -> {
                    Assertions.assertThat(director.getId())
                            .as("id режиссера должен совпадать с ожидаемым")
                            .isEqualTo(directorId);
                    Assertions.assertThat(director.getName())
                            .as("имя режиссера должно совпадать с ожидаемым")
                            .isEqualTo(director.getName());
                });
    }

    @Test
    public void getAllDirectors_shouldReturnAllDirectors() {
        Director secondDirector = Director.builder()
                .name("NewTestName")
                .build();

        secondDirector = directorRepository.create(secondDirector);

        Long firstDirectorId = director.getId();
        Long secondDirectorId = secondDirector.getId();

        Collection<Director> foundDirectors = directorRepository.findAll();

        Assertions.assertThat(foundDirectors)
                .isNotNull()
                .as("Должны вернуться %d режиссера с валидными полями", 2)
                .hasSize(2)
                .extracting(Director::getId, Director::getName)
                .containsExactlyInAnyOrder(
                        tuple(firstDirectorId, "TestName"),
                        tuple(secondDirectorId, "NewTestName")
                );
    }

    @Test
    public void updateDirector_shouldReturnDirectorWithNewData() {
        Long directorId = director.getId();

        Director updatedDirector = Director.builder()
                .id(directorId)
                .name("NewTestName")
                .build();

        directorRepository.update(updatedDirector);

        Optional<Director> foundDirector = directorRepository.findById(directorId);

        Assertions.assertThat(foundDirector)
                .hasValueSatisfying(director -> {
                    Assertions.assertThat(director.getName())
                            .as("имя режиссера должно совпадать с ожидаемым")
                            .isEqualTo(updatedDirector.getName());
                });
    }

    @Test
    public void deleteDirector_shouldReturnNoDirectors() {
        Long directorId = director.getId();

        directorRepository.delete(directorId);

        Collection<Director> foundDirectors = directorRepository.findAll();

        Assertions.assertThat(foundDirectors)
                .as("Должен вернуться пустой список режиссеров")
                .isEmpty();
    }
}
