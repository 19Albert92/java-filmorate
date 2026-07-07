package ru.yandex.practicum.filmorate.dto.review;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateReviewRequestTest {
    private static Validator validator;

    private static final CreateReviewRequest createReviewRequest = new CreateReviewRequest();

    @BeforeAll
    public static void setUp() {

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        createReviewRequest.setContent("Review content");
        createReviewRequest.setFilmId(10L);
        createReviewRequest.setUserId(10L);
        createReviewRequest.setIsPositive(true);
    }

    @Test
    public void testContentField() {

        createReviewRequest.setContent(null);

        Set<ConstraintViolation<CreateReviewRequest>> violations = validator.validate(createReviewRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Текст отзыва обязателен")));

        createReviewRequest.setContent("");

        violations = validator.validate(createReviewRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Текст отзыва не должен быть пустым")));

        createReviewRequest.setContent("New Review content");

        violations = validator.validate(createReviewRequest);

        assertTrue(violations.isEmpty(), "Ошибок не должно быть");
    }

    @Test
    public void testIsPositiveField() {

        createReviewRequest.setIsPositive(null);

        Set<ConstraintViolation<CreateReviewRequest>> violations = validator.validate(createReviewRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Тип отзыва обязателен")));

        createReviewRequest.setIsPositive(true);

        violations = validator.validate(createReviewRequest);

        assertTrue(violations.isEmpty(), "Ошибок не должно быть");
    }

    @Test
    public void testUserIdField() {

        createReviewRequest.setUserId(null);

        Set<ConstraintViolation<CreateReviewRequest>> violations = validator.validate(createReviewRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Идентификатор пользователя обязателен")));

        createReviewRequest.setUserId(10L);

        violations = validator.validate(createReviewRequest);

        assertTrue(violations.isEmpty(), "Ошибок не должно быть");
    }

    @Test
    public void testFilmIdField() {

        createReviewRequest.setFilmId(null);

        Set<ConstraintViolation<CreateReviewRequest>> violations = validator.validate(createReviewRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Идентификатор фильма обязателен")));

        createReviewRequest.setFilmId(10L);

        violations = validator.validate(createReviewRequest);

        assertTrue(violations.isEmpty(), "Ошибок не должно быть");
    }
}
