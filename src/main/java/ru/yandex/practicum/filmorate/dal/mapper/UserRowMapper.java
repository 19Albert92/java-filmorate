package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet result, int rowNum) throws SQLException {
        return User.builder()
                .id(result.getLong("id"))
                .name(result.getString("name"))
                .email(result.getString("email"))
                .birthday(LocalDate.parse(result.getString("birthday")))
                .login(result.getString("login"))
                .build();
    }
}
