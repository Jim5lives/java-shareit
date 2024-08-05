package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;

public interface ItemService {
    ItemDto createItem(Integer userId, NewItemRequest request);
    ItemDto findItemById(Integer id);
}
