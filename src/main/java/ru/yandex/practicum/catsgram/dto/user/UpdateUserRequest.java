package ru.yandex.practicum.catsgram.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = {"email"})
public class UpdateUserRequest {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Instant registrationDate;

    public boolean hasEmail() {
        return !email.isBlank();
    }

    public boolean hasPassword() {
        return !password.isBlank();
    }

    public boolean hasUsername() {
        return !username.isBlank();
    }
}
