package ru.yandex.practicum.filmorate.dto.user;

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

public class CreateUserRequestTest {

    private Validator validator;

    private static CreateUserRequest createUserRequest;

    @BeforeEach
    public void setUp() {

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        createUserRequest = new CreateUserRequest();
        createUserRequest.setName("User name");
        createUserRequest.setEmail("example@yandex.com");
        createUserRequest.setBirthday(LocalDate.of(1980, 1, 1));
        createUserRequest.setLogin("custom_login");
    }

    @Test
    @DisplayName("Проверка поле email на соответсвие заданной валидации")
    public void testNameField() {

        createUserRequest.setEmail(null);

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("поле email должен быть указан")));

        createUserRequest.setEmail("example_email");

        violations = validator.validate(createUserRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("email не прошел валидацию")));

        createUserRequest.setEmail("example@yandex.com");

        violations = validator.validate(createUserRequest);

        assertTrue(violations.isEmpty(), "Ошибок не должно быть");
    }

    @Test
    @DisplayName("Проверка поле login на соответсвие заданной валидации")
    public void testLoginField() {

        createUserRequest.setLogin(null);

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("поле login должен быть указан")));

        createUserRequest.setLogin("");

        violations = validator.validate(createUserRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("login пришел пустым")));

        createUserRequest.setLogin("custom login");

        violations = validator.validate(createUserRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("login не должен содержать пробелы")));

        createUserRequest.setLogin("custom_login");

        violations = validator.validate(createUserRequest);

        assertTrue(violations.isEmpty(), "Ошибок не должно быть");
    }

    @Test
    @DisplayName("Проверка поле birthday на соответсвие заданной валидации")
    public void testBirthdayField() {

        createUserRequest.setBirthday(null);

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("поле birthday должен быть указан")));

        createUserRequest.setBirthday(LocalDate.of(2027, 1, 1));

        violations = validator.validate(createUserRequest);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Дата рождения должна быть в прошлом")));

        createUserRequest.setBirthday(LocalDate.of(1980, 1, 1));

        violations = validator.validate(createUserRequest);

        assertTrue(violations.isEmpty(), "Ошибок не должно быть");
    }
}
