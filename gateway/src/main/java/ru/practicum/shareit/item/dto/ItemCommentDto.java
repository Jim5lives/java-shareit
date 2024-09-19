package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemCommentDto extends ItemDto {
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;
}
