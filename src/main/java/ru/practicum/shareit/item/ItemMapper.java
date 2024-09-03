package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static Item mapToItem(NewItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setAvailable(request.getAvailable());
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setOwnerId(item.getOwnerId());
        if (item.getRequest() != null) {
            dto.setRequestId(item.getRequest().getId());
        }
        return dto;
    }

    public static ItemCommentDto mapToItemAllDto(Item item, BookingDto lastBooking,
                                                 BookingDto nextBooking, List<CommentDto> commentsDto) {
        ItemCommentDto allDto = new ItemCommentDto();
        allDto.setId(item.getId());
        allDto.setName(item.getName());
        allDto.setDescription(item.getDescription());
        allDto.setAvailable(item.getAvailable());
        allDto.setOwnerId(item.getOwnerId());
        allDto.setRequestId(item.getRequest() != null ? item.getRequest().getId() : null);
        allDto.setLastBooking(lastBooking);
        allDto.setNextBooking(nextBooking);
        allDto.setComments(commentsDto);
        return allDto;
    }
}
