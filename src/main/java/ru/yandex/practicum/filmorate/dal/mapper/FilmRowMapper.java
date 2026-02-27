package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet result, int rowNum) throws SQLException {

        Mpa mpa = new Mpa();
        mpa.setId(result.getInt("mpa_id"));
        mpa.setName(result.getString("mpa_name"));

        return Film.builder()
                .id(result.getLong("id"))
                .name(result.getString("name"))
                .description(result.getString("description"))
                .duration(result.getLong(("duration")))
                .releaseDate(LocalDate.parse(result.getString("release_date")))
                .mpa(mpa)
                .build();
    }
}
