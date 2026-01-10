package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.groups.Default;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.util.validate.OnUpdate;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FilmTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Групповая валидация корректного фильма без ID (создание) и с ID (обновление)")
    public void shouldValidateFilmWithoutIdOrWithId() {
        Film film = Film.builder()
                .name("Остаться в живых")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(190L)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(),
                "Ошибок быть не должно, так как ID не обязателен для группы по умолчанию");

        violations = validator.validate(film, OnUpdate.class, Default.class);

        assertTrue(
                violations.stream()
                        .anyMatch(v ->
                                v.getMessage().equals("id не пришел")),
                "id не пришел (при обновлении обязателен)");
    }

    @Test
    @DisplayName("Групповая валидация название фильма: не пустое, не null значение")
    public void shouldValidateCorrectName() {
        Film film = Film.builder()
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(190L)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("name не пришел")));

        film.setName("");

        violations = validator.validate(film);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("name не может быть пустым")));
    }

    @Test
    @DisplayName("Групповая валидация описание фильма: не более 200 символов")
    public void shouldValidateCorrectDescription() {
        Film film = Film.builder()
                .name("Остаться в живых")
                .description("Авиалайнер терпит крушение на таинственном острове в океане. Сорок восемь уцелевших" +
                        " пассажиров оказываются в полной изоляции. Им предстоит не только выжить, но и разгадать" +
                        " пугающие секреты этого странного места...")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(190L)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.stream()
                .anyMatch(v ->
                        v.getMessage().equals("максимальная длина описания — 200 символов")));
    }

    @Test
    @DisplayName("Групповая валидация даты релиза фильма: не null значение и не раньше 28 декабря 1895 года")
    public void shouldValidateCorrectReleaseDate() {
        Film film = Film.builder()
                .name("Остаться в живых")
                .duration(190L)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("releaseDate не пришел")));

        film.setReleaseDate(LocalDate.of(1893, 10, 28));

        violations = validator.validate(film);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("дата релиза должна быть не раньше 28 декабря 1895 года")));
    }

    @Test
    @DisplayName("Групповая валидация продолжительности фильма: не null значение, только положительное число")
    public void shouldValidateCorrectDuration() {
        Film film = Film.builder()
                .name("Остаться в живых")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("duration не пришел")));

        film.setDuration(-100L);

        violations = validator.validate(film);

        assertTrue(violations.stream()
                .anyMatch(v ->
                        v.getMessage().equals("Продолжительность фильма должна быть больше 0")));
    }
}
