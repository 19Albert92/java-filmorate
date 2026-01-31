package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import ru.yandex.practicum.filmorate.util.validate.OnUpdate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Getter
public class User implements Identifiable {
    @NotNull(groups = OnUpdate.class, message = "Id не пришел или пришел пустым")
    private Long id;
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
    private Set<Long> friends;

    public void toggleFriend(Long friendId) {
        if (this.friends == null) {
            this.friends = new HashSet<>();
            this.friends.add(friendId);
        } else {
            this.friends.add(friendId);
        }
    }
}
