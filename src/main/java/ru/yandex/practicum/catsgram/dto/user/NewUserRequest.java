package ru.yandex.practicum.catsgram.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = {"email"})
public class NewUserRequest {
    private Long id;
    private String username;
    private String email;
    private String password;
}
