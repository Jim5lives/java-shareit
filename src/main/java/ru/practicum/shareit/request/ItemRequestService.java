package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createRequest(Integer userId, NewItemRequestRequest request);

    List<ItemRequestDto> getAllRequests(Integer userId);

    ItemRequestWithResponseDto getRequestById(Integer userId, Integer requestId);

    List<ItemRequestWithResponseDto> getAllUsersRequests(Integer userId);

}
