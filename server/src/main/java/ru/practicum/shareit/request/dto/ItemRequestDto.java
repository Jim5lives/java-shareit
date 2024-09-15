package ru.practicum.shareit.request.dto;


import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class ItemRequestDto {
    private Integer id;
    private String description;
    private UserDto requestor;
    private LocalDateTime created;
}
