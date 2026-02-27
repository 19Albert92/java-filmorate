package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
public class FriendshipRepositoryImpl extends BaseRepository<User> implements ru.yandex.practicum.filmorate.dal.FriendshipRepository {

    private static final String FIND_ALL_FRIENDS_BY_USER_ID_QUERY = "SELECT u.* FROM users u JOIN friendship AS f ON u.id = f.friend_id WHERE f.user_id = ?";

    private static final String INSERT_ADD_FRIEND =
            "INSERT INTO friendship(user_id, friend_id, status) VALUES (?, ?, ?)";

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

    public void addFriend(Friendship friendship) {
        insert(INSERT_ADD_FRIEND,
                friendship.getUserId(),
                friendship.getFriendId(),
                friendship.getStatus().getValue()
        );

    }

    @Override
    public void removeFriend(Friendship friendship) {
        insert(REMOVE_FRIEND,
                friendship.getUserId(),
                friendship.getFriendId()
        );

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
