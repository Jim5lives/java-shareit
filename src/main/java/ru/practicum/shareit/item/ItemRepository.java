package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item createItem(Item item);

    Optional<Item> findItemById(Integer id);

    Item updateItem(Integer id, Item item);

    void deleteItem(Integer id);

    List<Item> getAllUsersItems(Integer userId);

    List<Item> search(String query);

}
