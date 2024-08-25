package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exceptions.AccessForbiddenException;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto createItem(Integer userId, NewItemRequest request) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + userId));
        Item item = ItemMapper.mapToItem(request);
        item.setOwnerId(owner.getId());

        item = itemRepository.createItem(item);
        log.info("Вещь успешно создана: {}", item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto findItemById(Integer id) {
        Item item = itemRepository.findItemById(id)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + id));
        log.info("Вещь найдена по id={}: {}", id, item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(Integer userId, Integer itemId, UpdateItemRequest request) {
        Item item = itemRepository.findItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + itemId));

        if (isOwnerValid(userId, itemId)) {
            log.trace("Валидация пройдена userId={} и itemId={} совпадают", userId, itemId);
            Item itemToUpdate = updateItemFields(item, request);
            Item updatedItem = itemRepository.updateItem(itemId, itemToUpdate);
            log.info("Вещь успешно обновлена: {}", updatedItem);
            return ItemMapper.mapToItemDto(updatedItem);

        } else {
            log.warn("Не совпадают userId={} и ownerId={} вещи для обновления", userId, item.getOwnerId());
            throw new AccessForbiddenException("Нет прав для редактирования вещи у пользователя с id=" + userId);
        }
    }

    @Override
    public void deleteItem(Integer userId, Integer itemId) {
        if (isOwnerValid(userId, itemId)) {
            log.trace("Валидация успешна userId={} и itemId={} совпадают", userId, itemId);
            itemRepository.deleteItem(itemId);

        } else {
            log.warn("Не совпадают userId={} и ownerId={} вещи для удаления", userId, itemId);
            throw new AccessForbiddenException("Нет прав для удаления вещи у пользователя с id=" + userId);
        }
    }

    @Override
    public List<ItemDto> getAllUsersItems(Integer userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + userId));

        log.info("Выводится список вещей пользователя с id={}", userId);
        return itemRepository.getAllUsersItems(owner.getId()).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> search(String query) {
        if (query.isBlank()) {
            log.info("Передана пустая строка для поиска");
            return List.of();
        }

        log.info("Выводится список вещей, содержащих '{}'", query);
        return itemRepository.search(query).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    private boolean isOwnerValid(Integer userId, Integer itemId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + userId));
        Item item = itemRepository.findItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + itemId));

        return item.getOwnerId().equals(owner.getId());
    }

    private Item updateItemFields(Item item, UpdateItemRequest request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }
        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }
        if (request.hasAvailable()) {
            item.setAvailable(request.getAvailable());
        }
        return item;
    }
}
