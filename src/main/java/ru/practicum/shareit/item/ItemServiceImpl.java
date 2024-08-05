package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exceptions.AccessForbiddenException;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto createItem(Integer userId, NewItemRequest request) {
        User owner = userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + userId));
        Item item = ItemMapper.mapToItem(request);
        item.setOwnerId(owner.getId());
        item = itemRepository.createItem(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto findItemById(Integer id) {
        Item item = itemRepository.findItemById(id)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + id));
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(Integer userId, Integer itemId, UpdateItemRequest request) {
        User owner = userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + userId));
        Item item = itemRepository.findItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + itemId));

        if (item.getOwnerId().equals(owner.getId())) {

            Item itemToUpdate = ItemMapper.updateItemFields(item, request);
            Item updatedItem = itemRepository.updateItem(itemId, itemToUpdate);
            return ItemMapper.mapToItemDto(updatedItem);

        } else {
            throw new AccessForbiddenException("Нет прав для редактирования вещи у пользователя с id=" + userId);
        }
    }

}
