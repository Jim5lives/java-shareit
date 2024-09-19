package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(Integer userId, NewItemRequest request) {
        return post("", userId, request);
    }

    public ResponseEntity<Object> updateItem(Integer userId, Integer itemId, UpdateItemRequest request) {
        return patch("/" + itemId, userId, request);
    }

    public ResponseEntity<Object> findItemById(Integer userId, Integer id) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> deleteItem(Integer userId, Integer id) {
        return delete("/" + id, userId);
    }

    public ResponseEntity<Object> getAllUsersItems(Integer userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> search(String text) {
        return get("/?text={text}", null, Map.of("text", text));
    }

    public ResponseEntity<Object> addComment(Integer userId, Integer itemId, NewCommentRequest request) {
        return post("/" + itemId + "/comment", userId, request);
    }
}
