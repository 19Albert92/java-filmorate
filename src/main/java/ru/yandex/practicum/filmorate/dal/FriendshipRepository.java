package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipRepository {

    boolean addFriend(Long id, Long friendId, FriendStatus status);

    void removeFriend(Long userId, Long otherId);

    List<User> getAllFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long otherId);

    boolean checkFriendships(Long userId, Long friendId);
}
