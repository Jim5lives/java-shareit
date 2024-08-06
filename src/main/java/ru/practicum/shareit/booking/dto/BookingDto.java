package ru.practicum.shareit.booking.dto;


import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.Instant;

@Data
public class BookingDto {
    private Integer id;
    private Instant bookingStart;
    private Instant bookingEnd;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;
}
