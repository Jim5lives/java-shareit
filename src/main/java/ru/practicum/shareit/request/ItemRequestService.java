package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;

public interface ItemRequestService {

    ItemRequestDto createRequest(Integer userId, NewItemRequestRequest request);
}
