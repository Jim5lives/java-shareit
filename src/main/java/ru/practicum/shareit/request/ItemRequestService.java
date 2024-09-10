package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createRequest(Integer userId, NewItemRequestRequest request);

    List<ItemRequestDto> getAllRequests(Integer userId);

    List<ItemRequestDto> getAllUsersRequests(Integer userId);
}
