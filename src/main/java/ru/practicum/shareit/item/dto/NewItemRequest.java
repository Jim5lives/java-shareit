package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.request.model.ItemRequest;

@Data
@Validated
public class NewItemRequest {
    @NotBlank @NotNull
    private String name;
    @NotBlank @NotNull
    private String description;
    @NotNull
    private boolean isAvailable;
    @NotNull
    private Integer ownerId;
    private ItemRequest request;
}
