package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateFilmRequestTest {

    private Validator validator;

    private static CreateFilmRequest createFilmRequest;

    @BeforeEach
    public void setUp() {

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        createFilmRequest = new CreateFilmRequest();
        createFilmRequest.setDescription("Film description");
        createFilmRequest.setDuration(200L);
        createFilmRequest.setName("Film name");
        createFilmRequest.setReleaseDate(LocalDate.of(2020, 1, 1));
    }

    @Test
    @DisplayName("Проверка поле name на соответсвие заданной валидации")
    public void testNameField() {

        createFilmRequest.setName(null);

        Set<ConstraintViolation<CreateFilmRequest>> violations = validator.validate(createFilmRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("поле name не пришел")));

        createFilmRequest.setName("");

        violations = validator.validate(createFilmRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("name не может быть пустым")));

        createFilmRequest.setName("Film name");

        violations = validator.validate(createFilmRequest);

        assertTrue(violations.isEmpty(), "Ошибок не должно быть");
    }

    @Test
    @DisplayName("Проверка поле description на соответсвие заданной валидации")
    public void testDescriptionFiles() {

        createFilmRequest.setDescription("Film description".repeat(200));

        Set<ConstraintViolation<CreateFilmRequest>> violations = validator.validate(createFilmRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("максимальная длина описания — 200 символов")));
    }

    @Test
    @DisplayName("Проверка поле releaseDate на соответсвие заданной валидации")
    public void testReleaseDateField() {

        createFilmRequest.setReleaseDate(null);

        Set<ConstraintViolation<CreateFilmRequest>> violations = validator.validate(createFilmRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("поле releaseDate не пришел")));

        createFilmRequest.setReleaseDate(LocalDate.of(1894, 1, 1));

        violations = validator.validate(createFilmRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("дата релиза должна быть не раньше 28 декабря 1895 года")));

        createFilmRequest.setReleaseDate(LocalDate.of(2020, 1, 1));

        violations = validator.validate(createFilmRequest);

        assertTrue(violations.isEmpty(), "Ошибок не должно быть");
    }

    @Test
    @DisplayName("Проверка поле duration на соответсвие заданной валидации")
    public void testDurationField() {

        createFilmRequest.setDuration(null);

        Set<ConstraintViolation<CreateFilmRequest>> violations = validator.validate(createFilmRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("поле duration не пришел")));

        createFilmRequest.setDuration(-200L);

        violations = validator.validate(createFilmRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Продолжительность фильма должна быть больше 0")));

        createFilmRequest.setDuration(200L);

        violations = validator.validate(createFilmRequest);

        assertTrue(violations.isEmpty(), "Ошибок не должно быть");
    }
}
