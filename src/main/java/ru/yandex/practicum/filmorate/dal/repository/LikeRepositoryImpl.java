package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

@Repository
public class LikeRepositoryImpl extends BaseRepository<Like> implements ru.yandex.practicum.filmorate.dal.LikeRepository {

    private static final String FIND_FILM_BY_ID_AND_USER_ID_QUERY = "SELECT * FROM film_likes WHERE film_id = ? AND user_id = ?";

    private static final String INSERT_LIKE_TO_FILM_QUERY = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";

    private static final String REMOVE_LIKE_QUERY = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";

    public LikeRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Like> mapper) {
        super(jdbcTemplate, mapper);
    }


    @Override
    public boolean isLikeExists(Long filmId, Long userId) {
        return findOne(FIND_FILM_BY_ID_AND_USER_ID_QUERY, filmId, userId).isPresent();
    }

    @Override
    public void addLike(Long filmId, Long userid) {
        insert(INSERT_LIKE_TO_FILM_QUERY, filmId, userid);
    }

    @Override
    public void deleteLike(Long filmId, Long userid) {
        update(REMOVE_LIKE_QUERY, filmId, userid);
    }
}
