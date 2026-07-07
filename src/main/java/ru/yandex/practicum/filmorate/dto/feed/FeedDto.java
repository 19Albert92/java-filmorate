package ru.yandex.practicum.filmorate.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedDto {

    Long eventId;
    Long userId;
    Long timestamp;
    EventType eventType;
    OperationType operation;
    Long entityId;
}
