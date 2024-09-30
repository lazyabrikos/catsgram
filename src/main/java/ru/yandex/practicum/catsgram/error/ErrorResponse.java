package ru.yandex.practicum.catsgram.error;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}
