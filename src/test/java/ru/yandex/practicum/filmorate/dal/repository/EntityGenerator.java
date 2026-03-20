package ru.yandex.practicum.filmorate.dal.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityGenerator {

    private static String generateText(int length) {
        return new Random().ints(97, 123)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private static final Long generateRandomDuration = new Random().nextLong(80, 200);

    public static List<User> generateUsers(int count) {

        List<User> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            users.add(
                    User.builder()
                            .name(generateText(14))
                            .login(generateText(10))
                            .email("test1@test.com")
                            .birthday(LocalDate.of(1990 + i, 1,  i + 1))
                            .build()
            );
        }

        return users;
    }

    public static List<Film> generateFilms(int count, Mpa mpa) {

        List<Film> films = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            films.add(
                    Film.builder()
                            .name(generateText(14))
                            .description(generateText(24))
                            .releaseDate(LocalDate.of(1990 + i, 4, i + 1))
                            .duration(generateRandomDuration)
                            .mpa(mpa)
                            .build()
            );
        }

        return films;
    }

    public static List<Review> generateReview(int count, List<User> createdUsers, List<Film> createdFilms) {

        List<Review> reviews = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < count; i++) {

            User randomUser = createdUsers.get(random.nextInt(createdUsers.size()));

            Film randomFilm = createdFilms.get(random.nextInt(createdFilms.size()));

            reviews.add(
                    Review.builder()
                            .content(generateText(24))
                            .filmId(randomFilm.getId())
                            .userId(randomUser.getId())
                            .isPositive(random.nextBoolean())
                            .useful(0)
                            .build()
            );
        }

        return reviews;
    }
}
