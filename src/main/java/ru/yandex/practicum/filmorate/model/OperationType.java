package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public enum OperationType {
    ADD("Операция добавление"),
    UPDATE("Операция обновление"),
    REMOVE("Операция удаление");

    private final String description;

    OperationType(String description) {
        this.description = description;
    }
}
