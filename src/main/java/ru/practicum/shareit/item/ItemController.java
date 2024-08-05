package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @Valid @RequestBody NewItemRequest request) {
        return itemService.createItem(userId, request);
    }

    @GetMapping("/{id}")
    public ItemDto findItemById(@PathVariable Integer id) {
        return itemService.findItemById(id);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @PathVariable Integer itemId,
                              @RequestBody UpdateItemRequest request) {
        return itemService.updateItem(userId, itemId, request);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @PathVariable Integer itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getAllUsersItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getAllUsersItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }
}
