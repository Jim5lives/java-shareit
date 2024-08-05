package ru.practicum.shareit.booking.dto;


import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

@Data
public class BookingDto {
    private Integer id;
    private Instant bookingStart;
    private Instant bookingEnd;
    private Item item;
    private User booker;
    private BookingStatus status;
}
