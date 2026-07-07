package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BaseRepository<User> implements UserRepository {

    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users";

    private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";

    private static final String INSERT_USER_QUERY =
            "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET email = ?, name = ?, birthday = ?, login = ? WHERE id = ?";

    private static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM users WHERE id = ?";

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<User> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Optional<User> findById(Long id) {
        return findOne(FIND_USER_BY_ID_QUERY, id);
    }

    @Override
    public User create(User data) {
        Long newId = insertWithKey(INSERT_USER_QUERY,
                data.getEmail(),
                data.getLogin(),
                data.getName(),
                data.getBirthday()
        );

        data.setId(newId);
        return data;
    }

    @Override
    public User update(User data) {

        int resUpdateRows = update(UPDATE_USER_QUERY,
                data.getEmail(),
                data.getName(),
                data.getBirthday(),
                data.getLogin(),
                data.getId()
        );

        if (resUpdateRows == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }

        return data;
    }

    @Override
    public Collection<User> findAll() {
        return findMany(FIND_ALL_USERS_QUERY);
    }

    @Override
    public void deleteById(Long id) {

        int rows = update(DELETE_USER_BY_ID_QUERY, id);

        if (rows == 0) {
            throw new InternalServerException("Не удалось удалить данные");
        }
    }
}
