package ru.yandex.practicum.catsgram.error;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
public class ErrorResponse {
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}
