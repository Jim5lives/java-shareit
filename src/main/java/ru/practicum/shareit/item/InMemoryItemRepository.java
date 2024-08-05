package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Integer, Item> itemMap;
    private Integer id = 1;

    @Override
    public Item createItem(Item item) {
        Integer itemId = generateId();
        item.setId(itemId);
        itemMap.put(itemId, item);
        log.trace("Вещь сохранена в память");
        return item;
    }

    @Override
    public Optional<Item> findItemById(Integer id) {
        return Optional.ofNullable(itemMap.get(id));
    }

    @Override
    public Item updateItem(Integer itemId, Item item) {
        return itemMap.put(itemId, item);
    }

    @Override
    public void deleteItem(Integer id) {
        itemMap.remove(id);
        log.trace("Вещь удалена из памяти");
    }

    @Override
    public List<Item> getAllUsersItems(Integer userId) {
        return itemMap.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .toList();
    }

    @Override
    public List<Item> search(String query) {
        return itemMap.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    private Integer generateId() {
        log.trace("Присвоен новый itemId={}", id);
        return id++;
    }
}
