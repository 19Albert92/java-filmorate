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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Валидация корректного пользователя без ID (создание)")
    void shouldValidateCorrectUserWithoutId() {
        User user = User.builder()
                .email("test@yandex.ru")
                .login("valid_login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(),
                "Ошибок быть не должно, так как ID не обязателен для группы по умолчанию");
    }

    @Test
    @DisplayName("Граничное условие: День рождения сегодня (должно быть в прошлом)")
    void shouldFailIfBirthdayIsToday() {
        User user = User.builder()
                .email("test@mail.ru")
                .login("login")
                .birthday(LocalDate.now())
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Дата рождения должна быть в прошлом")));
    }

    @Test
    @DisplayName("Валидация логина: пробелы, пустые строки и null значения")
    void shouldFailIfLoginIsBlankOrNull() {
        User user = User.builder()
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1990, 5, 5))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("login должен быть указан")));

        user.setLogin(" ");
        violations = validator.validate(user);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("login пришел пустым")));

        user.setLogin("login 12");
        violations = validator.validate(user);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("login не должен содержать пробелы")));
    }

    @Test
    @DisplayName("Валидация email: некорректный формат")
    void shouldFailIfEmailIsInvalid() {
        User user = User.builder()
                .email("invalid-email.com")
                .login("login")
                .birthday(LocalDate.of(1990, 5, 5))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("email не прошел валидацию")));
    }

    @Test
    @DisplayName("Групповая валидация: Проверка ID при обновлении")
    void shouldFailIfIdIsNullOnUpdateGroup() {
        User user = User.builder()
                .id(null)
                .email("test@mail.ru")
                .login("login")
                .build();
        user.setBirthday(LocalDate.of(1990, 5, 5));

        Set<ConstraintViolation<User>> violations = validator.validate(user, OnUpdate.class, Default.class);

        assertFalse(violations.isEmpty(), "При обновлении ID обязателен");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Id не пришел или пришел пустым")));
    }
}
