package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.DirectorRepository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

@Repository
public class DirectorRepositoryImpl extends BaseRepository<Director> implements DirectorRepository {
    private static final String INSERT_DIRECTORS_FOR_FILM_QUERY = """
            INSERT INTO films_directors (film_id, director_id) VALUES (?, ?)
            """;

    private static final String FIND_DIRECTORS_ALL_QUERY = """
            SELECT * FROM directors
            """;

    private static final String FIND_DIRECTOR_BY_ID_QUERY = """
            SELECT * FROM directors WHERE id = ?
            """;

    private static final String INSERT_DIRECTOR_QUERY = """
            INSERT INTO directors (name) VALUES (?)
            """;

    private static final String UPDATE_DIRECTOR_QUERY = """
            UPDATE directors SET name = ? WHERE id = ?
            """;

    private static final String DELETE_DIRECTOR_QUERY = """
            DELETE FROM directors WHERE id = ?
            """;

    private static final String FIND_DIRECTORS_BY_FILM_ID_QUERY = """
            SELECT d.*
            FROM directors AS d
            JOIN films_directors AS fd ON d.id = fd.director_id
            WHERE fd.film_id = ?
            """;

    private static final String DELETE_FILM_DIRECTORS_QUERY = """
            DELETE FROM films_directors WHERE film_id = ?
            """;

    public DirectorRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Director> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public void save(List<Object[]> directors) {
        batchUpdate(INSERT_DIRECTORS_FOR_FILM_QUERY, directors, 10);
    }

    @Override
    public Optional<Director> findById(Long id) {
        return findOne(FIND_DIRECTOR_BY_ID_QUERY, id);
    }

    @Override
    public List<Director> findAll() {
        return findMany(FIND_DIRECTORS_ALL_QUERY);
    }

    @Override
    public Director create(Director data) {
        long newId = insertWithKey(
                INSERT_DIRECTOR_QUERY,
                data.getName()
        );

        data.setId(newId);

        return data;
    }

    @Override
    public Director update(Director data) {
        int resUpdateRows = update(UPDATE_DIRECTOR_QUERY,
                data.getName(),
                data.getId()
        );

        if (resUpdateRows == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }

        return data;
    }

    @Override
    public boolean delete(Long id) {
        return update(DELETE_DIRECTOR_QUERY, id) > 0;
    }

    @Override
    public List<Director> findDirectorsByFilmId(Long id) {
        return findMany(FIND_DIRECTORS_BY_FILM_ID_QUERY, id);
    }

    @Override
    public void removeFilmDirectors(Long filmId) {
        update(DELETE_FILM_DIRECTORS_QUERY, filmId);
    }
}
