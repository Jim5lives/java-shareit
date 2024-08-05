package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.request.model.ItemRequest;

@Data
@Validated
public class NewItemRequest {
    @NotNull @NotBlank
    private String name;
    @NotNull @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private Integer ownerId;
    private ItemRequest request;
}
