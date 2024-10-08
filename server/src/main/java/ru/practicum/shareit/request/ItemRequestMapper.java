package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import org.mapstruct.*;


@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface ItemRequestMapper {

    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    @Mapping(target = "requestor", source = "user")
    ItemRequest mapToItemRequest(NewItemRequestRequest request, UserDto user);

    List<ItemRequestDto> mapToItemRequestDto(List<ItemRequest> requests);

    ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest);

    @Mapping(target = "items", source = "items")
    ItemRequestWithItemsDto mapToItemRequestWithResponseDto(ItemRequest itemRequest,
                                                            List<ItemDto> items);
}

