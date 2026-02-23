package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

@Component
public class InMemoryUserStorage extends BaseStorage<User> implements UserStorage {

    @Override
    public User create(User data) {

        User user = this.add(data);

        logger.debug("Пользователь успешно добавлен: {}", user);

        return user;
    }

    @Override
    public User update(User data) {

        logger.info("Пользователь успешно обновлен: {}", data);

        return dataList.put(data.getId(), data);
    }
}
