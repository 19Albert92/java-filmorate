package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Feed;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FeedRowMapper implements RowMapper<Feed> {

    @Override
    public Feed mapRow(ResultSet result, int rowNum) throws SQLException {
        return Feed.builder()
                .timestamp(result.getLong("timestamp"))
                .userId(result.getLong("user_id"))
                .eventId(result.getLong("event_id"))
                .operation(result.getString("operation"))
                .entityId(result.getLong("entity_id"))
                .eventType(result.getString("event_type"))
                .build();
    }
}
