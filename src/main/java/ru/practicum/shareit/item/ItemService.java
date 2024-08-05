package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Integer userId, NewItemRequest request);

    ItemDto findItemById(Integer id);

    ItemDto updateItem(Integer userId, Integer itemId, UpdateItemRequest request);

    void deleteItem(Integer userId, Integer itemId);

    List<ItemDto> getAllUsersItems(Integer userId);

    List<ItemDto> search(String query);

}
