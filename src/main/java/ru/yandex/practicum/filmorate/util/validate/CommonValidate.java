package ru.yandex.practicum.filmorate.util.validate;

import ru.yandex.practicum.filmorate.exception.NotValidParamException;
import ru.yandex.practicum.filmorate.exception.ParamNullPointerException;

public class CommonValidate {

    private CommonValidate() {
    }

    public static void checkNotNullAndPositive(Long param, String message) throws NotValidParamException {

        if (param == null) {
            throw new ParamNullPointerException(message);
        }

        if (param <= 0) {
            throw new NotValidParamException(message);
        }
    }

    public static void checkNotNullAndPositive(Integer param, String message) throws NotValidParamException {

        if (param == null) {
            throw new ParamNullPointerException(message);
        }

        if (param <= 0) {
            throw new NotValidParamException(message);
        }
    }
}
