package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Feed {
    Long eventId;
    Long userId;
    Long timestamp;
    String eventType;
    String operation;
    Long entityId;
}
