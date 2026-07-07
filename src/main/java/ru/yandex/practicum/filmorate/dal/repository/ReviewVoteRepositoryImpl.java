package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.ReviewVoteRepository;
import ru.yandex.practicum.filmorate.model.ReviewVote;

import java.util.Collection;

@Repository
public class ReviewVoteRepositoryImpl extends BaseRepository<ReviewVote> implements ReviewVoteRepository {

    private static final String QUERY_INSERT_VOTE = """
            MERGE INTO review_votes (user_id, review_id, vote_type) KEY (user_id, review_id) VALUES (?, ?, ?)
            """;

    private static final String QUERY_DELETE_VOTE = """
            DELETE FROM review_votes WHERE user_id = ? AND review_id = ? AND vote_type = ?;
            """;

    private static final String QUERY_FIND_VOTES_BY_REVIEW_ID = """
            SELECT * FROM review_votes WHERE review_id = ?
            """;

    private static final String QUERY_FIND_VOTE_BY_PARAMS = """
            SELECT * FROM review_votes WHERE user_id = ? AND review_id = ?
            """;

    public ReviewVoteRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<ReviewVote> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public void insertVote(Long userId, Long reviewId, Boolean isLike) {
        update(QUERY_INSERT_VOTE, userId, reviewId, isLike);
    }

    @Override
    public boolean deleteVote(Long userId, Long reviewId, Boolean isLike) {
        return update(QUERY_DELETE_VOTE, userId, reviewId, isLike) > 0;
    }

    @Override
    public boolean findById(Long userId, Long reviewId) {
        return findOne(QUERY_FIND_VOTE_BY_PARAMS, userId, reviewId).isEmpty();
    }

    @Override
    public Collection<ReviewVote> getVotes(Long reviewId) {
        return findMany(QUERY_FIND_VOTES_BY_REVIEW_ID, reviewId);
    }
}
