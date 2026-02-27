package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.practicum.filmorate.util.validate.OnUpdate;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {

    @NotNull(groups = OnUpdate.class, message = "Id не пришел или пришел пустым")
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public boolean hasEmail() {
        return email != null && !email.isBlank();
    }

    public boolean hasLogin() {
        return login != null && !login.isBlank();
    }

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasBirthday() {
        return birthday != null;
    }
}
