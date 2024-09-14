package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "request", ignore = true)
    Item mapToItem(NewItemRequest request);

    @Mapping(target = "request", source = "itemRequest")
    @Mapping(target = "description", source = "request.description")
    Item mapToItem(NewItemRequest request, ItemRequest itemRequest);

    @Mapping(target = "requestId", source = "item.request.id")
    ItemDto mapToItemDto(Item item);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "available", source = "item.available")
    @Mapping(target = "ownerId", source = "item.ownerId")
    @Mapping(target = "requestId", source = "item.request.id")
    @Mapping(target = "lastBooking", source = "lastBooking")
    @Mapping(target = "nextBooking", source = "nextBooking")
    @Mapping(target = "comments", source = "commentsDto")
    ItemCommentDto mapToItemCommentDto(Item item, BookingDto lastBooking, BookingDto nextBooking,
                                       List<CommentDto> commentsDto);
}
