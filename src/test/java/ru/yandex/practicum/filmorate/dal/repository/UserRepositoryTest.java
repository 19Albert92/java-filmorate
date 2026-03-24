package ru.yandex.practicum.filmorate.dal.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JdbcTest
@AutoConfigureTestDatabase
@Import({UserRepositoryImpl.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ =  @Autowired)
public class UserRepositoryTest {

    private final UserRepositoryImpl userRepository;

    private final List<User> users = new ArrayList<>();

    @BeforeEach
    void tearDown() {

        List<User> newUsers = EntityGenerator.generateUsers(6);

        newUsers.forEach(user -> users.add(userRepository.create(user)));
    }

    @Test
    public void should_find_user_by_id() {

        long userId = users.getFirst().getId();

        Optional<User> findById = userRepository.findById(userId);

        AssertionsForClassTypes.assertThat(findById)
                .isPresent()
                .as("Должен вернуть пользователя с id = %d", userId)
                .hasValueSatisfying(u ->
                        AssertionsForClassTypes.assertThat(u).hasFieldOrPropertyWithValue("id",userId)
                );
    }

    @Test
    public void should_find_two_users() {

        int expectUsersLength = users.size();

        Assertions.assertThat(users)
                .hasSize(expectUsersLength)
                .isNotEmpty()
                .as("Пользователей в базе должно быть %d", expectUsersLength)
                .contains(users.get(0), users.get(1));

    }

    @Test
    public void should_update_user_by_id() {

        String newName = "UpdatedName";
        LocalDate newBirthday = LocalDate.of(1997, 1, 1);
        User user = users.getFirst();
        long userId = user.getId();

        user.setName(newName);
        user.setBirthday(newBirthday);

        userRepository.update(user);
        Optional<User> updatedUser =  userRepository.findById(userId);

        AssertionsForClassTypes.assertThat(updatedUser)
                .as("Пользователь с id = %d должен быть после обновления", userId)
                .isPresent()
                .get()
                .satisfies(u -> {
                    AssertionsForClassTypes.assertThat(u.getName())
                            .as("Новое имя польователя должен быть %s", newName)
                            .isEqualTo(newName);
                    AssertionsForClassTypes.assertThat(u.getBirthday())
                            .as("Новое дата рождения польователя должен быть %s", newBirthday)
                            .isEqualTo(newBirthday);
                });
    }

    @Test
    public void should_delete_user_by_id() {

        long userId = users.getFirst().getId();

        userRepository.deleteById(userId);

        AssertionsForClassTypes.assertThat(userRepository.findById(userId))
                .isEmpty();
    }
}
