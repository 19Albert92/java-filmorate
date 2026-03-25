package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.ReviewVote;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewVoteRowMapper implements RowMapper<ReviewVote> {
    @Override
    public ReviewVote mapRow(ResultSet result, int rowNum) throws SQLException {
        return ReviewVote.builder()
                .reviewId(result.getLong("review_id"))
                .userId(result.getLong("user_id"))
                .voteType(result.getBoolean("vote_type"))
                .build();
    }
}
