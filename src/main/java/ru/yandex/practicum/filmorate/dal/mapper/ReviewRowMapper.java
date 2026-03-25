package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewRowMapper implements RowMapper<Review> {
    @Override
    public Review mapRow(ResultSet result, int rowNum) throws SQLException {
        return Review.builder()
                .id(result.getLong("id"))
                .content(result.getString("content"))
                .filmId(result.getLong("film_id"))
                .isPositive(result.getBoolean("is_positive"))
                .useful(result.getInt("useful"))
                .userId(result.getLong("user_id"))
                .build();
    }
}
