package ru.yandex.practicum.filmorate.dal.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FeedsRepository;
import ru.yandex.practicum.filmorate.model.Feed;

import java.util.Collection;

@Slf4j
@Repository
public class FeedsRepositoryImpl extends BaseRepository<Feed> implements FeedsRepository {

    private static final String INSERT_FEED_QUERY = """
            INSERT INTO feeds (user_id, timestamp, event_type, operation, entity_id) VALUES (?, ?, ?, ?, ?)
            """;

    private static final String FIND_ALL_FEEDS_BY_USER_ID_QUERY = """
            SELECT * FROM feeds WHERE user_id = ? ORDER BY timestamp ASC
            """;

    public FeedsRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Feed> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public void addFeed(Feed feed) {
        insertWithKey(INSERT_FEED_QUERY,
                feed.getUserId(),
                feed.getTimestamp(),
                feed.getEventType(),
                feed.getOperation(),
                feed.getEntityId()
        );
    }

    @Override
    public Collection<Feed> getFeeds(Long userid) {
        return findMany(FIND_ALL_FEEDS_BY_USER_ID_QUERY, userid);
    }
}
