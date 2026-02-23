package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.CrudMethodsStorage;

import java.util.Collection;

public interface UserService extends CrudMethodsStorage<User, Long> {

    User addFriend(Long id, Long friendId);

    User deleteFriend(Long id, Long friendId);

    Collection<User> getAllFriends(Long id);

    Collection<User> getAllCommonFriends(Long id, Long otherId);
}
