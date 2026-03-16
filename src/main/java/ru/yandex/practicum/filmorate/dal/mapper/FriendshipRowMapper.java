package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendshipRowMapper implements RowMapper<Friendship> {
    @Override
    public Friendship mapRow(ResultSet result, int rowNum) throws SQLException {

        return Friendship.builder()
                .userId(result.getLong("user_id"))
                .friendId(result.getLong("friend_id"))
                .status(FriendStatus.ACCEPTED)
                .build();
    }
}
