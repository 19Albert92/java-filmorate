package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.ReviewVote;

import java.util.Collection;

public interface ReviewVoteRepository {

    void insertVote(Long userId, Long reviewId, Boolean isLike);

    boolean deleteVote(Long userId, Long reviewId, Boolean isLike);

    boolean findById(Long userId, Long reviewId);

    Collection<ReviewVote> getVotes(Long reviewId);
}
