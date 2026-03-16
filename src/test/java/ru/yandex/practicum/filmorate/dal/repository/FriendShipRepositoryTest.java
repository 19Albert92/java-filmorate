package ru.yandex.practicum.filmorate.dal.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mapper.FriendshipRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@JdbcTest
@AutoConfigureTestDatabase
@Import({UserRepositoryImpl.class, UserRowMapper.class, FriendshipRepositoryImpl.class, FriendshipRowMapper.class})
@RequiredArgsConstructor(onConstructor_ =  @Autowired)
public class FriendShipRepositoryTest {

    private final UserRepository userRepository;

    private final FriendshipRepository friendshipRepository;

    private final List<User> users = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        List<User> newUsers = EntityGenerator.generateUsers(6);

        newUsers.forEach(user -> users.add(userRepository.create(user)));

        List<Friendship> friendships  = List.of(
                Friendship.builder()
                        .userId(users.get(0).getId())
                        .friendId(users.get(1).getId())
                        .status(FriendStatus.ACCEPTED)
                        .build(),
                Friendship.builder()
                        .userId(users.get(0).getId())
                        .friendId(users.get(2).getId())
                        .status(FriendStatus.ACCEPTED)
                        .build(),
                Friendship.builder()
                        .userId(users.get(1).getId())
                        .friendId(users.get(2).getId())
                        .status(FriendStatus.ACCEPTED)
                        .build(),
                Friendship.builder()
                        .userId(users.get(2).getId())
                        .friendId(users.get(1).getId())
                        .status(FriendStatus.ACCEPTED)
                        .build()
        );

        friendships.forEach(el -> friendshipRepository.addFriend(el.getUserId(), el.getFriendId(), el.getStatus()));
    }

    @Test
    public void should_return_all_friends() {

        User expectUserFriends = users.get(2);

        User resultFriend = users.get(1);

        List<User> resultFriendships = friendshipRepository.getAllFriends(expectUserFriends.getId());

        Assertions.assertThat(resultFriendships)
                .as("Должен придти списко из одного друга")
                .hasSize(1)
                .first()
                .satisfies(u -> Assertions.assertThat(u.getId()).isEqualTo(resultFriend.getId()));
    }

    @Test
    public void should_find_common_friends() {

        User user1 = users.get(0);

        User user2 = users.get(2);

        User expectedUser = users.get(1);

        List<User> resultFriendships = friendshipRepository.getCommonFriends(user1.getId(), user2.getId());

        Assertions.assertThat(resultFriendships)
                .as("Должен вернутся один общий друг")
                .hasSize(1)
                .first()
                .satisfies(u -> Assertions.assertThat(u.getId()).isEqualTo(expectedUser.getId()));

    }

    @Test
    public void should_remove_friend_from_user_list() {

        User removedUser = users.get(2);
        User toUser = users.get(0);

        friendshipRepository.removeFriend(toUser.getId(), removedUser.getId());

        List<User> userFriends = friendshipRepository.getAllFriends(toUser.getId());

        Assertions.assertThat(userFriends)
                .as("Должен остаться один друг")
                .hasSize(1)
                .first()
                .satisfies(u -> Assertions.assertThat(u.getId()).isNotEqualTo(removedUser.getId()));

    }
}
