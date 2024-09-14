package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Integer userId, NewItemRequest request);

    ItemCommentDto findItemById(Integer userId, Integer id);

    ItemDto updateItem(Integer userId, Integer itemId, UpdateItemRequest request);

    void deleteItem(Integer userId, Integer itemId);

    List<ItemCommentDto> getAllUsersItems(Integer userId);

    List<ItemDto> search(String query);

    CommentDto addComment(Integer userId, Integer itemId, NewCommentRequest request);
}
