package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
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

    @Override
    @Transactional
    public ItemRequestDto createRequest(Integer userId, NewItemRequestRequest request) {
        User user = validateUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(request, user);
        itemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getAllRequests(Integer userId) {
        validateUser(userId);
        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdNot(userId);
        return ItemRequestMapper.mapToItemRequest(requests);
    }

    @Override
    public List<ItemRequestDto> getAllUsersRequests(Integer userId) {
        return List.of();
    }

    private User validateUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id =" + userId));
    }
}
