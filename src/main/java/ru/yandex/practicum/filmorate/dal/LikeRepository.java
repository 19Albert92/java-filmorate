package ru.yandex.practicum.filmorate.dal;

public interface LikeRepository {

    boolean isLikeExists(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userid);

    void addLike(Long filmId, Long userid);
}
