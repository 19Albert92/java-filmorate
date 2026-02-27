package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRowMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet result, int rowNum) throws SQLException {

        return Mpa.builder()
                .id(result.getInt("id"))
                .name(result.getString("name"))
                .build();
    }
}
