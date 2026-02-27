package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.CreateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userStorage;

    private final FriendshipRepository friendshipStorage;

    public UserServiceImpl(UserRepository userStorage, FriendshipRepository friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    @Override
    public List<UserDto> addFriend(Long id, Long friendId) {

        checkUsersExist(id, friendId);

        friendshipStorage.addFriend(
                new Friendship(id, friendId, FriendStatus.ACCEPTED)
        );

        List<User> friendsFromDb = friendshipStorage.getAllFriends(id);

        return friendsFromDb.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public List<UserDto> deleteFriend(Long id, Long friendId) {

        checkUsersExist(id, friendId);

        friendshipStorage.removeFriend(new Friendship(id, friendId, FriendStatus.ACCEPTED));

        List<User> friendsFromDb = friendshipStorage.getAllFriends(id);

        return friendsFromDb.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public Collection<UserDto> getAllFriends(Long id) {

        checkUsersExist(id);

        List<User> friendsFromDb = friendshipStorage.getAllFriends(id);

        return friendsFromDb.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public Collection<UserDto> getAllCommonFriends(Long id, Long otherId) {

        checkUsersExist(id, otherId);

        List<User> friendsFromDb = friendshipStorage.getCommonFriends(id, otherId);

        return friendsFromDb.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto findById(Long id) {
        return userStorage.findById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    @Override
    public UserDto create(CreateUserRequest request) {

        User user = UserMapper.mapToUser(request);

        user = userStorage.create(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto update(UpdateUserRequest request) {

        User user = userStorage.findById(request.getId())
                .map(u -> UserMapper.mapToUpdateFields(u, request))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user = userStorage.update(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public Collection<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    private void checkUsersExist(Long... users) {
        for (Long id : users) {
            userStorage.findById(id)
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        }
    }
}
