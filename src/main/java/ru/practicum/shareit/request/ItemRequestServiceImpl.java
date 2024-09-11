package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemRequestDto createRequest(Integer userId, NewItemRequestRequest request) {
        User user = validateUser(userId);
        ItemRequest itemRequest = itemRequestMapper.mapToItemRequest(request, user);
        itemRequest = itemRequestRepository.save(itemRequest);
        return itemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getAllRequests(Integer userId) {
        validateUser(userId);
        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdNot(userId);
        return itemRequestMapper.mapToItemRequestDto(requests);
    }

    @Override
    public ItemRequestWithResponseDto getRequestById(Integer userId, Integer requestId) {
        validateUser(userId);
        List<ItemDto> items = itemRepository.findByRequestId(requestId).stream()
                .map(itemMapper::mapToItemDto)
                .toList();
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Не найден запрос с id =" + requestId));
        return itemRequestMapper.mapToItemRequestWithResponseDto(itemRequest, items);
    }

    @Override
    public List<ItemRequestWithResponseDto> getAllUsersRequests(Integer userId) {
        validateUser(userId);
        List<ItemRequest> requests = itemRequestRepository.findByRequestorId(userId);

        return requests.stream()
                .map(ItemRequest::getId)
                .map(id -> getRequestById(userId, id))
                .toList();
    }

    private User validateUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id =" + userId));
    }
}
