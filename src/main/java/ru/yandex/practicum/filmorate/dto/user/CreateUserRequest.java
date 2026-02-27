package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUserRequest {
    @NotNull(message = "email должен быть указан")
    @Email(message = "email не прошел валидацию")
    private String email;
    @NotNull(message = "login должен быть указан")
    @NotBlank(message = "login пришел пустым")
    @Pattern(regexp = "^\\S+$", message = "login не должен содержать пробелы")
    private String login;
    private String name;
    @NotNull(message = "birthday должен быть указан")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;
}
