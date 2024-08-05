package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

public interface ItemRepository {

    Item createItem(Item item);
    Optional<Item> findItemById(Integer id);
}
