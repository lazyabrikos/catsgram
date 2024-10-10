package ru.yandex.practicum.catsgram.dto.post;

import lombok.Data;

import java.time.Instant;

@Data
public class UpdatePostRequest {
    private Long id;
    private Long authorId;
    private String description;
    private Instant postDate;

    public boolean hasAuthorId() {
        return authorId != null;
    }

    public boolean hasDescription() {
        return !description.isBlank();
    }

}
