package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository extends CrudMethodsRepository<User, Long> {
    public List<User> getUsersWithSameLikes(Long userId);
}
