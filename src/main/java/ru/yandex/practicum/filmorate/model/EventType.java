package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public enum EventType {

    LIKE("Событие добавление/удаление лайка"),
    REVIEW("Событие добавление/удаление отзыва на фильм"),
    FRIEND("Событие добавление/удаление друзей");

    final String description;

    EventType(String description) {
        this.description = description;
    }
}
