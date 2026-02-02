package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User addFriend(Long id, Long friendId) {

        User findUser = userStorage.findById(id);

        User findFriendUser = userStorage.findById(friendId);

        findUser.addFriend(friendId);

        findFriendUser.addFriend(id);

        return userStorage.update(findUser);
    }

    @Override
    public User deleteFriend(Long id, Long friendId) {

        User findUser = userStorage.findById(id);

        User findFriendUser = userStorage.findById(friendId);

        findUser.deleteFriend(friendId);

        findFriendUser.deleteFriend(id);

        return userStorage.update(findUser);
    }

    @Override
    public Collection<User> getAllFriends(Long id) {

        User findUser = userStorage.findById(id);

        return findUser.getFriends()
                .stream()
                .map(userStorage::findById)
                .toList();
    }

    @Override
    public Collection<User> getAllCommonFriends(Long id, Long otherId) {

        User findUser = userStorage.findById(id);

        User findOtherUser = userStorage.findById(otherId);

        Set<Long> userFriends = findUser.getFriends();

        return findOtherUser.getFriends()
                .stream()
                .filter(userFriends::contains)
                .map(userStorage::findById)
                .toList();
    }

    @Override
    public User findById(Long id) {
        return userStorage.findById(id);
    }

    @Override
    public User create(User data) {

        if (data.getName() == null || data.getName().isBlank()) {
            data.setName(data.getLogin());
        }

        return userStorage.create(data);
    }

    @Override
    public User update(User data) {

        User findUser = userStorage.findById(data.getId());

        findUser.setEmail(data.getEmail());
        findUser.setLogin(data.getLogin());
        findUser.setBirthday(data.getBirthday());

        if ((data.getName() == null || !data.getName().isBlank()) && findUser.getName().equals(findUser.getLogin())) {
            findUser.setName(data.getLogin());
        } else {
            findUser.setName(data.getName());
        }

        return userStorage.update(findUser);
    }

    @Override
    public Collection<User> findAll() {
        return userStorage.findAll();
    }
}
