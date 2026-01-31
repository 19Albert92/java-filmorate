package ru.yandex.practicum.filmorate.util.validate.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.util.validate.MinDateValidator;

import java.time.LocalDate;

public class MinDateValidatorImpl implements ConstraintValidator<MinDateValidator, LocalDate> {

    private static final LocalDate CINEMA_BIRTH_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {

        if (localDate == null) {
            return true;
        }

        return !localDate.isBefore(CINEMA_BIRTH_DATE);
    }
}
