package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.user.CreateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;

import java.util.Collection;

public interface UserService {

    UserDto findById(Long id);

    UserDto create(CreateUserRequest data);

    UserDto update(UpdateUserRequest data);

    Collection<UserDto> findAll();

    boolean addFriend(Long id, Long friendId);

    boolean deleteFriend(Long id, Long friendId);

    Collection<UserDto> getAllFriends(Long id);

    Collection<UserDto> getAllCommonFriends(Long id, Long otherId);

    void delete(Long userId);
}
