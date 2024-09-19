package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;


@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public Object createRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                @Valid @RequestBody NewItemRequestRequest request) {
        log.info("Получен запрос на добавление запроса вещи {}", request);
        return itemRequestClient.createRequest(userId, request);
    }

    @GetMapping("/all")
    public Object getAllRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Получен запрос на вывод всех запросов пользователей, кроме userId={}", userId);
        return itemRequestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public Object getRequestById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable Integer requestId) {
        log.info("Получен запрос на вывод запроса вещи с id={}", requestId);
        return itemRequestClient.getRequestById(userId, requestId);
    }

    @GetMapping
    public Object getAllUsersRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Получен запрос на всех запросов пользователя с id={}", userId);
        return itemRequestClient.getAllUsersRequests(userId);
    }
}
