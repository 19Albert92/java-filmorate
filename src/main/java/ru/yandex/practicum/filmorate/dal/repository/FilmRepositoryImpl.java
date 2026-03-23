package ru.yandex.practicum.filmorate.dal.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class FilmRepositoryImpl extends BaseRepository<Film> implements ru.yandex.practicum.filmorate.dal.FilmRepository {

    private static final String FIND_FILMS_ALL_QUERY = """
            SELECT f.*, m.name AS mpa_name
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            """;

    private static final String FIND_FILM_BY_ID_QUERY = """
            SELECT f.*, m.name AS mpa_name
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            WHERE f.id = ?
            """;

    private static final String INSERT_FILM_QUERY =
            "INSERT INTO films (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_FILM_QUERY =
            "UPDATE films SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? WHERE ID = ?";

    private static final String FIND_POPULAR_FILMS_BY_LIMIT_QUERY = """
            SELECT f.*, m.name AS mpa_name
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN film_likes AS fl ON f.id = fl.film_id
            GROUP BY f.id, m.name
            ORDER BY COUNT(fl.user_id) DESC
            LIMIT ?
            """;

    private static final String FIND_FILM_RECOMMENDATIONS_QUERY = """
            SELECT f.*, m.name AS mpa_name
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            WHERE f.id in (
                SELECT DISTINCT film_id
                FROM film_likes
                WHERE user_id in (
                    SELECT user_id
                    FROM film_likes
                    WHERE film_id IN (
                        SELECT film_id
                        FROM film_likes
                        WHERE user_id = ?
                    )
                    AND user_id IS NOT NULL
                    AND user_id <> ?
                    GROUP BY user_id
                    ORDER BY count(film_id)
                    DESC limit 10
                )
                AND film_id is not null
                AND film_id NOT IN (
                    select film_id
                    from film_likes
                    where user_id = ?
                )
                LIMIT 15
            )
            """;

    public FilmRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Film> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Optional<Film> findById(Long id) {
        return findOne(FIND_FILM_BY_ID_QUERY, id);
    }

    @Override
    public Film create(Film data) {
        long newId = insertWithKey(INSERT_FILM_QUERY,
                data.getName(),
                data.getDescription(),
                data.getReleaseDate(),
                data.getDuration(),
                data.getMpa().getId()
        );

        data.setId(newId);

        return data;
    }

    @Override
    public Film update(Film data) {

        int resUpdateRows = update(UPDATE_FILM_QUERY,
                data.getName(),
                data.getDescription(),
                data.getReleaseDate(),
                data.getDuration(),
                data.getMpa().getId(),
                data.getId()
        );

        if (resUpdateRows == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }

        return data;
    }

    @Override
    public Collection<Film> findAll() {
        return findMany(FIND_FILMS_ALL_QUERY);
    }

    @Override
    public List<Film> getPopularFilmByLikes(Integer limit) {
        return findMany(FIND_POPULAR_FILMS_BY_LIMIT_QUERY, limit);
    }

    @Override
    public List<Film> getRecommendations(Long userId)  {
        return findMany(FIND_FILM_RECOMMENDATIONS_QUERY, userId, userId, userId);
    }
}
