package ru.yandex.practicum.filmorate.model;

import java.util.Map;

public record ErrorResponse(String error, Map<String, String> details) {
}
