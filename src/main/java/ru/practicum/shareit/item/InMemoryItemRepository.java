package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Map;

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
        return item;
    }

    private Integer generateId() {
        log.info("Сгенерирован новый itemId={}", id);
        return id++;
    }
}
