package ru.yandex.practicum.filmorate.exception;

public class EmptyParamException extends RuntimeException {
    public EmptyParamException(String message) {
        super(message);
    }
}
