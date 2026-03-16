package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.user.CreateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserDto mapToUserDto(User user) {

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setBirthday(user.getBirthday());

        if (user.getName() == null || user.getName().isBlank()) {
            userDto.setName(user.getLogin());
        } else {
            userDto.setName(user.getName());
        }

        return userDto;
    }

    public static User mapToUser(CreateUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .login(request.getLogin())
                .birthday(request.getBirthday())
                .name(request.getName())
                .build();
    }

    public static User mapToUpdateFields(User user, UpdateUserRequest request) {
        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }

        if (request.hasLogin()) {
            user.setLogin(request.getLogin());
        }

        if (request.hasBirthday()) {
            user.setBirthday(request.getBirthday());
        }

        if (request.hasName()) {
            user.setName(request.getName());
        } else {
            user.setName(request.getLogin());
        }

        return user;
    }

}
