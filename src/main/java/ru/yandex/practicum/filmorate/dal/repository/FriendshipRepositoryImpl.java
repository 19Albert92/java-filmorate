package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
public class FriendshipRepositoryImpl extends BaseRepository<User> implements FriendshipRepository {

    private static final String FIND_ALL_FRIENDS_BY_USER_ID_QUERY = "SELECT u.* FROM users u JOIN friendship AS f ON u.id = f.friend_id WHERE f.user_id = ?";

    private static final String INSERT_ADD_FRIEND =
            "INSERT INTO friendship (user_id, friend_id, status) VALUES (?, ?, ?)";

    private static final String FIND_FRIENDSHIP =
            "SELECT COUNT(*) FROM friendship WHERE user_id = ? AND friend_id = ?";

    private static final String REMOVE_FRIEND = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";

    private static final String FIND_COMMON_FRIENDS_QUERY = """
           SELECT u.*
           FROM users u
           JOIN friendship f1 ON u.id = f1.friend_id
           JOIN friendship f2 ON u.id = f2.friend_id
           WHERE f1.user_id = ?
           AND f2.user_id = ?
           """;

    public FriendshipRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<User> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public boolean addFriend(Long userId, Long friendId, FriendStatus status) {
        return update(INSERT_ADD_FRIEND, userId,friendId,status.getValue());
    }

    @Override
    public boolean checkFriendships(Long userId, Long friendId) {
        return findCount(FIND_FRIENDSHIP, userId, friendId) > 0;
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        update(REMOVE_FRIEND, userId, friendId);
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        return findMany(FIND_ALL_FRIENDS_BY_USER_ID_QUERY, userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        return findMany(FIND_COMMON_FRIENDS_QUERY, userId, otherId);
    }
}
