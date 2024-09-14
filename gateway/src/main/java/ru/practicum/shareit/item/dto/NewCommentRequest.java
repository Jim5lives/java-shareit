package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class NewCommentRequest {
    @NotEmpty
    private String text;
}
