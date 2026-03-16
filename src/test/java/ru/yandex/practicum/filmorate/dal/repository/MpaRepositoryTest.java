package ru.yandex.practicum.filmorate.dal.repository;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@JdbcTest
@AutoConfigureTestDatabase
@Import({MpaRepositoryImpl.class, MpaRowMapper.class})
@RequiredArgsConstructor(onConstructor_ =  @Autowired)
public class MpaRepositoryTest {

    private final MpaRepository mpaRepository;

    @Test
    public void should_mpa_by_id() {

        final Mpa expectedMpa = Mpa.builder().id(1).name("G").build();

        Optional<Mpa> mpaFined = mpaRepository.findById(expectedMpa.getId());

        AssertionsForClassTypes.assertThat(mpaFined)
                .as("Рейтинг с ID %d должен существовать", expectedMpa.getId())
                .isPresent()
                .get()
                .satisfies(mpa ->
                        Assertions.assertThat(mpa.getName())
                                .as("Должен вернуть рейтинг %s", expectedMpa.getName())
                                .isEqualTo(expectedMpa.getName()));



        Mpa expectedMpa2 = Mpa.builder().id(51).name("PG-132").build();

        mpaFined = mpaRepository.findById(expectedMpa2.getId());

        AssertionsForClassTypes.assertThat(mpaFined)
                .as("Рейтинг с ID %d должен нет", expectedMpa2.getId())
                .isEmpty();
    }

    @Test
    public void should_all_mpa() {

        List<Mpa> mpas = mpaRepository.findAll();

        Assertions.assertThat(mpas)
                .as("Количество рейтингов должно быть равна 5")
                .hasSize(5)
                .first()
                .satisfies(mpa ->
                        Assertions.assertThat(mpa.getId()).as("Рейтинг с id: 1 должен быть первым")
                                .isEqualTo(1));

    }
}
