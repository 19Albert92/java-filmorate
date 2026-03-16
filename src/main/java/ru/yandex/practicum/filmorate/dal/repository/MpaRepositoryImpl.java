package ru.yandex.practicum.filmorate.dal.repository;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Log4j2
@Repository
public class MpaRepositoryImpl extends BaseRepository<Mpa> implements ru.yandex.practicum.filmorate.dal.MpaRepository {

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";

    private static final String FIND_All_QUERY = "SELECT * FROM mpa";

    public MpaRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Mpa> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Optional<Mpa> findById(Integer mpaId) {
        return findOne(FIND_BY_ID_QUERY, mpaId);
    }

    @Override
    public List<Mpa> findAll() {
        return findMany(FIND_All_QUERY);
    }
}
