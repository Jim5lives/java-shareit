package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

@Data
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private boolean isAvailable;
    private Integer ownerId;
    private ItemRequest request;
}
