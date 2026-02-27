package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipRepository {

    void addFriend(Friendship friendship);

    void removeFriend(Friendship friendship);

    List<User> getAllFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long otherId);
}
