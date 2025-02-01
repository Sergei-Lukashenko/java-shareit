package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestForComment;


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

    public ResponseEntity<Object> getItemsByOwner(Integer ownerId) {
        return get("/", ownerId);
    }

    public ResponseEntity<Object> getItemById(Integer ownerId, Integer itemId) {
        return get("/" + itemId, ownerId);
    }

    public ResponseEntity<Object> create(Integer ownerId, ItemDto itemDto) {
        return post("", ownerId, itemDto);
    }

    public ResponseEntity<Object> update(Integer ownerId, ItemDto itemDto) {
        return patch("/" + itemDto.getId(), ownerId, itemDto);
    }

    public ResponseEntity<Object> searchForItem(String text) {
        return get("?text=" + text);
    }

    public ResponseEntity<Object> addComment(Integer userId, Integer itemId, RequestForComment text) {
        return post("/" + itemId + "/comment", userId, text);
    }
}
