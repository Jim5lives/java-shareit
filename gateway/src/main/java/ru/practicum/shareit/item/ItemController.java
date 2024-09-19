package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;


@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public Object createItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                             @Valid @RequestBody NewItemRequest request) {
        log.info("Получен запрос на создание вещи {}", request);
        return itemClient.createItem(userId, request);
    }

    @GetMapping("/{id}")
    public Object findItemById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                               @PathVariable Integer id) {
        log.info("Получен запрос на поиск вещи по id={}", id);
        return itemClient.findItemById(userId, id);
    }

    @PatchMapping("/{itemId}")
    public Object updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                             @PathVariable Integer itemId,
                             @RequestBody UpdateItemRequest request) {
        log.info("Получен запрос на обновление вещи с id={}", itemId);
        return itemClient.updateItem(userId, itemId, request);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @PathVariable Integer itemId) {
        log.info("Получен запрос на удаление вещи с id={}", itemId);
        itemClient.deleteItem(userId, itemId);
    }

    @GetMapping
    public Object getAllUsersItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Получен запрос на вывод вещей пользователя с id={}", userId);
        return itemClient.getAllUsersItems(userId);
    }

    @GetMapping("/search")
    public Object search(@RequestParam String text) {
        log.info("Получен запрос на поиск вещей по подстроке: {}", text);
        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public Object addComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable Integer itemId,
                                 @Valid @RequestBody NewCommentRequest request) {
        log.info("Получен запрос на добавление комментария: {}", request);
        return itemClient.addComment(userId, itemId, request);
    }
}
