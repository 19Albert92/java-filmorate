package ru.yandex.practicum.filmorate.dal.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<T> {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<T> mapper;

    protected List<T> findMany(String query) {
        return jdbcTemplate.query(query, mapper);
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbcTemplate.query(query, mapper, params);
    }

    protected Integer findCount(String query, Object... params) {
        try {

            return jdbcTemplate.queryForObject(query, Integer.class, params);

        } catch (EmptyResultDataAccessException exception) {
            return 0;
        }
    }

    protected Optional<T> findOne(String query, Object... params) {
        try {

            T t = jdbcTemplate.queryForObject(query, mapper, params);

            return Optional.ofNullable(t);

        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    protected long insertWithKey(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            return preparedStatement;
        }, keyHolder);

        Long newId =  keyHolder.getKeyAs(Long.class);

        if (newId != null) {
            return newId;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    protected Integer update(String query, Object... params) {
        return jdbcTemplate.update(query, params);
    }

    protected void batchUpdate(String query, List<Object[]> batchArgs, int batchSize) {
        jdbcTemplate.batchUpdate(query, batchArgs, batchSize,
                (ps,  args) -> {
                    for (int i = 0; i < args.length; i++) {
                        ps.setObject(i + 1, args[i]);
                    }
                });
    }
}
