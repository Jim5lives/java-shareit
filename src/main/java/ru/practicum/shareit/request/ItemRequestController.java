package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                        @RequestBody NewItemRequestRequest request) {
        return itemRequestService.createRequest(userId, request);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestService.getAllRequests(userId);
    }

    @GetMapping()
    public List<ItemRequestDto> getAllUsersRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestService.getAllUsersRequests(userId);
    }
}
