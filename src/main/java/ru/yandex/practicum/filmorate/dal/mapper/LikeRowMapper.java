package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikeRowMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet result, int rowNum) throws SQLException {

        Like like = new Like();
        like.setFilmId(result.getLong("film_id"));
        like.setUserId(result.getLong("user_id"));

        return like;
    }
}
