package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Feed;

import java.util.Collection;

public interface FeedsRepository {

    void addFeed(Feed feed);

    Collection<Feed> getFeeds(Long userid);
}
