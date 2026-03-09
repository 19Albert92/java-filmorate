package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.util.validate.OnUpdate;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateUserRequestTest {

    private Validator validator;

    private static UpdateUserRequest updateUserRequest;

    @BeforeEach
    public void setUp() {

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName("User name");
        updateUserRequest.setEmail("example@yandex.com");
        updateUserRequest.setBirthday(LocalDate.of(1980, 1, 1));
        updateUserRequest.setLogin("custom_login");
    }

    @Test
    @DisplayName("Проверка поле id на соответсвие заданной валидации")
    public void testDescriptionFiles() {

        updateUserRequest.setId(null);

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(updateUserRequest,  OnUpdate.class);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Id не пришел или пришел пустым")));
    }
}
