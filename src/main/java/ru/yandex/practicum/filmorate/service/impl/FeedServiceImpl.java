package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FeedsRepository;
import ru.yandex.practicum.filmorate.dto.feed.FeedDto;
import ru.yandex.practicum.filmorate.mapper.FeedMapper;
import ru.yandex.practicum.filmorate.service.FeedService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;

@Log4j2
@Service
public class FeedServiceImpl implements FeedService {

    private final FeedsRepository feedsRepository;

    public FeedServiceImpl(FeedsRepository feedsRepository) {
        this.feedsRepository = feedsRepository;
    }

    @Override
    public void addFeed(FeedDto feedDto) {
        log.info("Operation: {}, Event: {}",
                feedDto.getOperation().getDescription(), feedDto.getEventType().getDescription());

        feedDto.setTimestamp(Timestamp.from(Instant.now()).getTime());

        feedsRepository.addFeed(FeedMapper.mapFeed(feedDto));
    }

    @Override
    public Collection<FeedDto> getAllFeeds(Long userId) {
        return feedsRepository.getFeeds(userId).stream()
                .map(FeedMapper::mapFeedDto)
                .toList();
    }
}
