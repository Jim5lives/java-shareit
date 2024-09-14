package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                        @Valid @RequestBody NewItemRequestRequest request) {
        log.info("Получен запрос на добавление запроса вещи {}", request);
        return itemRequestService.createRequest(userId, request);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Получен запрос на вывод всех запросов пользователей, кроме userId={}", userId);
        return itemRequestService.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithResponseDto getRequestById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                     @PathVariable Integer requestId) {
        log.info("Получен запрос на вывод запроса вещи с id={}", requestId);
        return itemRequestService.getRequestById(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestWithResponseDto> getAllUsersRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Получен запрос на всех запросов пользователя с id={}", userId);
        return itemRequestService.getAllUsersRequests(userId);
    }
}
