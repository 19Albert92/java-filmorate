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
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmRepositoryImpl.class, FilmRowMapper.class, GenreRepositoryImpl.class, GenreRowMapper.class})
@RequiredArgsConstructor(onConstructor_ =  @Autowired)
public class GenderRepositoryTest {

    private final GenreRepository genreRepository;

    private final FilmRepository filmRepository;

    private final List<Film> films = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        Mpa mpa = Mpa.builder().id(1).build();

        List<Film> newFilms = EntityGenerator.generateFilms(4, mpa);

        newFilms.forEach(film -> films.add(filmRepository.create(film)));

        List<Object[]> genresFirstFilm = Stream.of(1,2,4)
                .map(id -> new Object[]{films.getFirst().getId(), id}).toList();

        List<Object[]> genresLastFilm = Stream.of(4,5,1,2)
                .map(id -> new Object[]{films.get(1).getId(), id}).toList();

        genreRepository.save(genresFirstFilm);
        genreRepository.save(genresLastFilm);
    }

    @Test
    public void should_return_genre_by_id() {

        String expected_genre = "Комедия";

        Optional<Genre> finedGenre = genreRepository.findById(1);

        AssertionsForClassTypes.assertThat(finedGenre)
                .as("Жанр должен вернутся")
                .isPresent()
                .as("Жанр должен быть: %s", expected_genre)
                .get()
                .satisfies(genre ->
                        Assertions.assertThat(genre.getName()).isEqualTo(expected_genre)
                );

        finedGenre = genreRepository.findById(12);

        AssertionsForClassTypes.assertThat(finedGenre)
                .as("Такого жанра нет")
                .isEmpty();
    }

    @Test
    public void should_return_all_genre() {

        List<Genre> genre = genreRepository.findAll();

        Assertions.assertThat(genre)
                .as("Жанров должно быть 6")
                .hasSize(6);
    }

    @Test
    public void should_return_genre_by_film() {

        String last_genre = "Триллер";

        Film film = films.getFirst();

        List<Genre> genres = genreRepository.findByFilmId(film.getId());

        Assertions.assertThat(genres)
                .as("Жанров должно быть 3")
                .hasSize(3)
                .last()
                .satisfies(genre ->
                        Assertions.assertThat(genre.getName()).isEqualTo(last_genre)
                );
    }
}
