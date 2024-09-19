package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAllByOwnerId(Integer ownerId);

    @Query("SELECT i FROM Item i " +
            "WHERE (UPPER(i.name) LIKE UPPER(CONCAT('%', :query, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', :query, '%'))) " +
            "AND i.available = true")
    List<Item> search(@Param("query") String query);

    List<Item> findByRequestId(Integer requestId);
}
