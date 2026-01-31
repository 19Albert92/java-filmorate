package ru.yandex.practicum.filmorate.util.validate;

import ru.yandex.practicum.filmorate.exception.NotValidParamException;

public class CommonValidate {

    private CommonValidate() {
    }

    public static void checkNotNegative(Long param, String message) throws NotValidParamException {
        if (param <= 0) {
            throw new NotValidParamException(message);
        }
    }

    public static void checkNotNegative(Integer param, String message) throws NotValidParamException {
        if (param <= 0) {
            throw new NotValidParamException(message);
        }
    }
}
