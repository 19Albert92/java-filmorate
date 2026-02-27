package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryImpl extends BaseRepository<Genre> implements ru.yandex.practicum.filmorate.dal.GenreRepository {

    private static final String INSERT_GENRE = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";

    private static final String FIND_BY_FILM_ID_QUERY = """
            SELECT g.id, g.name
            FROM genre AS g
            JOIN film_genre AS fg ON g.id = fg.genre_id
            WHERE fg.film_id = ?
            GROUP BY g.id
            """;

    public GenreRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Genre> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public void save(List<Object[]> genres) {
        batchUpdate(INSERT_GENRE, genres, 10);
    }

    @Override
    public Optional<Genre> findById(Integer genreId) {
        return findOne(FIND_BY_ID_QUERY, genreId);
    }

    @Override
    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public List<Genre> findByFilmId(Long filmId) {
        return findMany(FIND_BY_FILM_ID_QUERY, filmId);
    }
}
