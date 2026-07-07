package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.feed.FeedDto;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.OperationType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedMapper {

    public static Feed mapFeed(FeedDto feedDto) {
        return Feed.builder()
                .operation(feedDto.getOperation().name())
                .eventType(feedDto.getEventType().name())
                .entityId(feedDto.getEntityId())
                .userId(feedDto.getUserId())
                .eventId(feedDto.getEventId())
                .timestamp(feedDto.getTimestamp())
                .build();
    }

    public static FeedDto mapFeedDto(Feed feed) {

        FeedDto feedDto = new FeedDto();

        feedDto.setEntityId(feed.getEntityId());
        feedDto.setEventId(feed.getEventId());
        feedDto.setUserId(feed.getUserId());
        feedDto.setTimestamp(feed.getTimestamp());
        feedDto.setOperation(OperationType.valueOf(feed.getOperation()));
        feedDto.setEventType(EventType.valueOf(feed.getEventType()));

        return feedDto;
    }
}
