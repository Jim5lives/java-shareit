package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {
    //TODO add MapStruct

    public static ItemRequest mapToItemRequest(NewItemRequestRequest request, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(request.getDescription());
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    public static ItemRequest mapToItemRequest(ItemRequestDto dto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(dto.getId());
        itemRequest.setDescription(dto.getDescription());
        itemRequest.setCreated(dto.getCreated());
        itemRequest.setRequestor(dto.getRequestor());
        return itemRequest;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setCreated(itemRequest.getCreated());
        dto.setRequestor(itemRequest.getRequestor());
        return dto;
    }
}
