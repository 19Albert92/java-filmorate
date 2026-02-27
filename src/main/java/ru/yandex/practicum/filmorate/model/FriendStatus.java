package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public enum FriendStatus {
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED");

    private final String value;

    FriendStatus(String status) {
        this.value = status;
    }
}
