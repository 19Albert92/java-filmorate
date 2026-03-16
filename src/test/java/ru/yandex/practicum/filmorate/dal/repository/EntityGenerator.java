package ru.yandex.practicum.filmorate.dal.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

}
