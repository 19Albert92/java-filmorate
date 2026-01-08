package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.utill.Identifiable;
import ru.yandex.practicum.filmorate.model.utill.OnUpdate;

import java.time.LocalDate;

@Data
public class User implements Identifiable {
    @NotNull(groups = OnUpdate.class, message = "Id не пришел или пришел пустым")
    private Long id;
    @NotNull(message = "email должен быть указан")
    @Email(message = "email не прошел валидацию")
    private String email;
    @NotNull(message = "login должен быть указан")
    @NotBlank(message = "login пришел пустым 11111")
    private String login;
    private String name;
    @NotNull(message = "birthday должен быть указан")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;
}
