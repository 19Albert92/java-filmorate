package ru.yandex.practicum.filmorate.exception;

public class NotValidParamException extends RuntimeException {
    public NotValidParamException(String message) {
        super(message);
    }
}
