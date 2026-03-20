package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.ReviewVoteRepository;
import ru.yandex.practicum.filmorate.dto.review.ReviewVoteRequest;
import ru.yandex.practicum.filmorate.service.ReviewVoteService;


@Service
public class ReviewVoteServiceImpl implements ReviewVoteService {

    private final ReviewVoteRepository reviewVoteRepository;

    public ReviewVoteServiceImpl(ReviewVoteRepository reviewVoteRepository) {
        this.reviewVoteRepository = reviewVoteRepository;
    }

    @Override
    public void addVoteState(ReviewVoteRequest request) {
        reviewVoteRepository.insertVote(request.getUserId(), request.getReviewId(), request.getIsLike());
    }

    @Override
    public boolean removeVoteState(ReviewVoteRequest request) {
        return reviewVoteRepository.deleteVote(request.getUserId(), request.getReviewId(), request.getIsLike());
    }

    @Override
    public boolean hasVoteState(ReviewVoteRequest reviewVoteRequest) {
        return reviewVoteRepository.findById(reviewVoteRequest.getUserId(),
                reviewVoteRequest.getReviewId());
    }
}