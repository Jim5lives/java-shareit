package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

@Data
public class ItemRequest {
    Integer id;
    String description;
    User requestor;
    Instant created;
}

