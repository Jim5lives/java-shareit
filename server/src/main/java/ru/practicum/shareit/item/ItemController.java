package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @Valid @RequestBody NewItemRequest request) {
        log.info("Получен запрос на создание вещи {}", request);
        return itemService.createItem(userId, request);
    }

    @GetMapping("/{id}")
    public ItemCommentDto findItemById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                       @PathVariable Integer id) {
        log.info("Получен запрос на поиск вещи по id={}", id);
        return itemService.findItemById(userId, id);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @PathVariable Integer itemId,
                              @RequestBody UpdateItemRequest request) {
        log.info("Получен запрос на обновление вещи с id={}", itemId);
        return itemService.updateItem(userId, itemId, request);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @PathVariable Integer itemId) {
        log.info("Получен запрос на удаление вещи с id={}", itemId);
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping
    public List<ItemCommentDto> getAllUsersItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Получен запрос на вывод вещей пользователя с id={}", userId);
        return itemService.getAllUsersItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("Получен запрос на поик вещей по подстроке: {}", text);
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable Integer itemId,
                                 @Valid @RequestBody NewCommentRequest request) {
        log.info("Получен запрос на добавление комментария: {}", request);
        return itemService.addComment(userId, itemId, request);
    }
}
