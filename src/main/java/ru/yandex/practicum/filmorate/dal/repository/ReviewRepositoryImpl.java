package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

@Repository
public class ReviewRepositoryImpl extends BaseRepository<Review> implements ReviewRepository {

    private static final String QUERY_FIND_ALL_BY_LIMIT = """
            SELECT * FROM reviews ORDER BY useful DESC LIMIT ?
            """;
    private static final String QUERY_FIND_ALL = """
            SELECT * FROM reviews ORDER BY useful DESC
            """;
    private static final String QUERY_FIND_ALL_BY_FILM_ID = """
            SELECT * FROM reviews WHERE film_id = ? ORDER BY useful DESC LIMIT ?
            """;

    private static final String QUERY_FIND_BY_ID = """
            SELECT * FROM reviews WHERE id = ?
            """;

    private static final String QUERY_DELETE_BY_ID = """
            DELETE FROM reviews WHERE id = ?
            """;

    private static final String QUERY_INSERT_REVIEW = """
            INSERT INTO reviews (content, is_positive, user_id, film_id) VALUES (?, ?, ?, ?)
            """;

    private static final String QUERY_UPDATE_REVIEW = """
            UPDATE reviews SET content = ?, is_positive = ? WHERE id = ?
            """;

    private static final String QUERY_UPDATE_USEFUL = """
            UPDATE reviews SET useful = useful + ? WHERE id = ?
            """;

    public ReviewRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Review> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Collection<Review> findAll(Long filmId, Integer count) {
        return findMany(QUERY_FIND_ALL_BY_FILM_ID, filmId, count);
    }

    @Override
    public Collection<Review> findAll(Integer count) {
        return findMany(QUERY_FIND_ALL_BY_LIMIT, count);
    }

    @Override
    public boolean deleteReview(Long reviewId) {
        return update(QUERY_DELETE_BY_ID, reviewId) > 0;
    }

    @Override
    public Optional<Review> findById(Long id) {
        return findOne(QUERY_FIND_BY_ID, id);
    }

    @Override
    public Review create(Review data) {
        data.setId(insertWithKey(QUERY_INSERT_REVIEW,
                data.getContent(),
                data.getIsPositive(),
                data.getUserId(),
                data.getFilmId())
        );
        return data;
    }

    @Override
    public Review update(Review data) {
        update(QUERY_UPDATE_REVIEW, data.getContent(), data.getIsPositive(), data.getId());
        return data;
    }

    @Override
    public void updateUseful(Integer useful, Long reviewId) {
        update(QUERY_UPDATE_USEFUL, useful, reviewId);
    }

    @Override
    public Collection<Review> findAll() {
        return findMany(QUERY_FIND_ALL);
    }
}
