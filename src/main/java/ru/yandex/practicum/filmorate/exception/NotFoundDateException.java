package ru.yandex.practicum.filmorate.exception;

public class NotFoundDateException extends RuntimeException {
    public NotFoundDateException(String message) {
        super(message);
    }
}
