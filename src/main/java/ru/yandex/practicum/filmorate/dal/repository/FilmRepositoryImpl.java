package ru.yandex.practicum.filmorate.dal.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Collections;
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

    private static final String DELETE_FILM_BY_ID_QUERY = "DELETE FROM films WHERE id = ?";

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

    private static final String FIND_MOST_POPULAR_FILMS_BY_GENRE_AND_YEAR_QUERY = """
            SELECT DISTINCT f.*, m.name AS mpa_name
            FROM films f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            WHERE f.id IN (
                SELECT film_id
                FROM film_likes
                WHERE
                film_id IN (
                    SELECT film_id
                    FROM film_genres
                    WHERE genre_id = ?
                )
                AND
                film_id IN (
                    SELECT id
                    FROM films
                    WHERE EXTRACT(YEAR FROM release_date) = ?
                )
                GROUP BY film_id
                ORDER BY count(film_id) DESC
            )
            LIMIT ?
            """;

    private static final String FIND_MOST_POPULAR_FILMS_BY_GENRE_QUERY = """
            SELECT DISTINCT f.*, m.name AS mpa_name
            FROM films f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            WHERE f.id IN (
                SELECT film_id
                FROM film_likes
                WHERE
                film_id IN (
                    SELECT film_id
                    FROM film_genres
                    WHERE genre_id = ?
                )
                GROUP BY film_id
                ORDER BY count(film_id) DESC
            )
            LIMIT ?
            """;

    private static final String FIND_MOST_POPULAR_FILMS_BY_YEAR_QUERY = """
            SELECT DISTINCT f.*, m.name AS mpa_name
            FROM films f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            WHERE f.id IN (
                SELECT film_id
                FROM film_likes
                WHERE
                film_id IN (
                    SELECT id
                    FROM films
                    WHERE EXTRACT(YEAR FROM release_date) = ?
                )
                GROUP BY film_id
                ORDER BY count(film_id) DESC
            )
            LIMIT ?
            """;

    private static final String FIND_COMMON_FILMS_QUERY = """
            SELECT f.*, m.name AS mpa_name
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            JOIN film_likes AS fl_u ON f.id = fl_u.film_id AND fl_u.user_id = ?
            JOIN film_likes AS fl_f ON f.id = fl_f.film_id AND fl_f.user_id = ?
            LEFT JOIN film_likes AS fl_all ON f.id = fl_all.film_id
            GROUP BY f.id, m.name
            ORDER BY COUNT(DISTINCT fl_all.user_id) DESC
            """;

    private static final String FIND_POPULAR_FILMS_QUERY = """
            SELECT f.*, m.name AS mpa_name
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN film_likes AS fl ON f.id = fl.film_id
            GROUP BY f.id, m.name
            ORDER BY COUNT(fl.user_id) DESC
            """;

    private static final String FIND_FILTERED_BY_TITLE_AND_DIRECTOR_FILMS_QUERY = """
            SELECT f.*, m.name AS mpa_name, COUNT(DISTINCT fl.user_id) AS likes_count
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN films_directors AS fd ON f.id = fd.film_id
            LEFT JOIN directors AS d ON fd.director_id = d.id
            LEFT JOIN film_likes AS fl ON f.id = fl.film_id
            WHERE f.name ILIKE ?
            OR d.name ILIKE ?
            GROUP BY f.id, m.name
            ORDER BY likes_count DESC, f.id DESC
            """;

    private static final String FIND_FILTERED_BY_TITLE_FILMS_QUERY = """
            SELECT f.*, m.name AS mpa_name, COUNT(fl.user_id) AS likes_count
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN film_likes AS fl ON f.id = fl.film_id
            WHERE f.name ILIKE ?
            GROUP BY f.id, m.name
            ORDER BY likes_count DESC, f.id DESC
            """;

    private static final String FIND_FILTERED_BY_DIRECTOR_FILMS_QUERY = """
            SELECT f.*, m.name AS mpa_name, COUNT(DISTINCT fl.user_id) AS likes_count
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            JOIN films_directors AS fd ON f.id = fd.film_id
            JOIN directors AS d ON fd.director_id = d.id
            LEFT JOIN film_likes AS fl ON f.id = fl.film_id
            WHERE d.name ILIKE ?
            GROUP BY f.id, m.name
            ORDER BY likes_count DESC, f.id DESC
            """;

    private static final String FIND_FILMS_BY_DIRECTOR_ID_QUERY = """
            SELECT DISTINCT f.*, m.name AS mpa_name
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN films_directors AS fd ON f.id = fd.film_id
            LEFT JOIN directors AS d ON fd.director_id = d.id
            WHERE d.id = ?
            """;

    private static final String FIND_FILMS_BY_DIRECTOR_ID_SORTED_BY_YEAR_QUERY = """
            SELECT f.*, m.name AS mpa_name
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN films_directors AS fd ON f.id = fd.film_id
            LEFT JOIN directors AS d ON fd.director_id = d.id
            WHERE d.id = ?
            ORDER BY f.release_date
            """;

    private static final String FIND_FILMS_BY_DIRECTOR_ID_SORTED_BY_LIKES_QUERY = """
            SELECT f.*, m.name AS mpa_name
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            LEFT JOIN films_directors AS fd ON f.id = fd.film_id
            LEFT JOIN directors AS d ON fd.director_id = d.id
            LEFT JOIN film_likes AS fl ON f.id = fl.film_id
            WHERE d.id = ?
            GROUP BY f.id, m.name
            ORDER BY COUNT(fl.user_id) DESC
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
    public List<Film> getRecommendations(Long userId) {
            return findMany(FIND_FILM_RECOMMENDATIONS_QUERY, userId, userId, userId);
    }

    @Override
    public List<Film> getPopularFilmByLikes() {
        return findMany(FIND_POPULAR_FILMS_QUERY);
    }

    @Override
    public List<Film> getFilteredByTitleAndDirectorFilms(String query) {
        String queryParam = "%" + query + "%";
        return findMany(FIND_FILTERED_BY_TITLE_AND_DIRECTOR_FILMS_QUERY, queryParam, queryParam);
    }

    @Override
    public List<Film> getFilteredByTitleFilms(String query) {
        String queryParam = "%" + query + "%";
        return findMany(FIND_FILTERED_BY_TITLE_FILMS_QUERY, queryParam);
    }

    @Override
    public List<Film> getFilteredByDirectorFilms(String query) {
        String queryParam = "%" + query + "%";
        return findMany(FIND_FILTERED_BY_DIRECTOR_FILMS_QUERY, queryParam);
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        return findMany(FIND_COMMON_FILMS_QUERY, userId, friendId);
    }

    @Override
    public List<Film> getFilmsByDirectorId(Long id) {
        return findMany(FIND_FILMS_BY_DIRECTOR_ID_QUERY, id);
    }

    @Override
    public List<Film> getFilmsByDirectorIdSortedByYear(Long id) {
        return findMany(FIND_FILMS_BY_DIRECTOR_ID_SORTED_BY_YEAR_QUERY, id);
    }

    @Override
    public List<Film> getFilmsByDirectorIdSortedByLikes(Long id) {
        return findMany(FIND_FILMS_BY_DIRECTOR_ID_SORTED_BY_LIKES_QUERY, id);
    }

    @Override
    public List<Film> getMostPopulars(Integer count, Long genreId, Long year) {
        if (year != 0 && genreId != 0) {
            return findMany(FIND_MOST_POPULAR_FILMS_BY_GENRE_AND_YEAR_QUERY, genreId, year, count);
        } else if (year == 0 && genreId != 0) {
            return findMany(FIND_MOST_POPULAR_FILMS_BY_GENRE_QUERY, genreId, count);
        } else if (year != 0) {
            return findMany(FIND_MOST_POPULAR_FILMS_BY_YEAR_QUERY, year, count);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteById(Long id) {

        int rows = update(DELETE_FILM_BY_ID_QUERY, id);

        if (rows == 0) {
            throw new InternalServerException("Не удалось удалить данные");
        }
    }
}
