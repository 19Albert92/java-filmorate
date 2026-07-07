package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.feed.FeedDto;

import java.util.Collection;

public interface FeedService {

    void addFeed(FeedDto feedDto);

    Collection<FeedDto> getAllFeeds(Long userId);
}
