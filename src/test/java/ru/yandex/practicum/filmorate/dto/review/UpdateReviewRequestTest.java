package ru.yandex.practicum.filmorate.dto.review;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateReviewRequestTest {

    private static Validator validator;

    private static final UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest();

    @BeforeAll
    public static void setUp() {

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        updateReviewRequest.setContent("Review content");
        updateReviewRequest.setReviewId(10L);
        updateReviewRequest.setIsPositive(true);
    }

    @Test
    public void testContentField() {

        updateReviewRequest.setContent(null);

        Set<ConstraintViolation<UpdateReviewRequest>> violations = validator.validate(updateReviewRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Текст отзыва обязателен")));

        updateReviewRequest.setContent("");

        violations = validator.validate(updateReviewRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Текст отзыва не должен быть пустым")));

        updateReviewRequest.setContent("New Review content");

        violations = validator.validate(updateReviewRequest);

        assertTrue(violations.isEmpty(), "Ошибок не должно быть");
    }

    @Test
    public void testPreviewIdField() {

        updateReviewRequest.setReviewId(null);

        Set<ConstraintViolation<UpdateReviewRequest>> violations = validator.validate(updateReviewRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Идентификатор отзыва обязателен")));

        updateReviewRequest.setReviewId(-10L);

        violations = validator.validate(updateReviewRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Идентификатор отзыва не должен быть мень нуля")));

        updateReviewRequest.setReviewId(10L);

        violations = validator.validate(updateReviewRequest);

        assertTrue(violations.isEmpty(), "Ошибок не должно быть");
    }
}
