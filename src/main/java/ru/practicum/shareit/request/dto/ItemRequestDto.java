package ru.practicum.shareit.request.dto;


import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

@Data
public class ItemRequestDto {
    private Integer id;
    private String description;
    private User requestor;
    private Instant created;
}
