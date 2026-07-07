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

public class UpdateFIlmRequestTest {

    private Validator validator;

    private static UpdateFilmRequest updateFilmRequest;

    @BeforeEach
    public void setUp() {

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        updateFilmRequest = new UpdateFilmRequest();
        updateFilmRequest.setDescription("Film description");
        updateFilmRequest.setDuration(200L);
        updateFilmRequest.setName("Film name");
        updateFilmRequest.setReleaseDate(LocalDate.of(2020, 1, 1));
    }

    @Test
    @DisplayName("Проверка поле id на соответсвие заданной валидации")
    public void testDescriptionFiles() {

        updateFilmRequest.setId(null);

        Set<ConstraintViolation<UpdateFilmRequest>> violations = validator.validate(updateFilmRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Id не пришел или пришел пустым")));
    }
}
