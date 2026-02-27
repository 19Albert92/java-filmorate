package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.user.CreateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {

    UserDto findById(Long id);

    UserDto create(CreateUserRequest data);

    UserDto update(UpdateUserRequest data);

    Collection<UserDto> findAll();

    List<UserDto> addFriend(Long id, Long friendId);

    List<UserDto> deleteFriend(Long id, Long friendId);

    Collection<UserDto> getAllFriends(Long id);

    Collection<UserDto> getAllCommonFriends(Long id, Long otherId);
}
