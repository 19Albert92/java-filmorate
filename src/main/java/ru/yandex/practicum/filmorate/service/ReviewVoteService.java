package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.review.ReviewVoteRequest;

public interface ReviewVoteService {

    void addVoteState(ReviewVoteRequest reviewVoteRequest);

    boolean removeVoteState(ReviewVoteRequest reviewVoteRequest);

    boolean hasVoteState(ReviewVoteRequest reviewVoteRequest);
}
