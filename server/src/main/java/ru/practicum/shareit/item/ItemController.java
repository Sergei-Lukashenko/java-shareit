package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String USERID_HEADER = "X-Sharer-User-Id";

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<Collection<ItemDto>> findAll(@RequestHeader(USERID_HEADER) Long ownerId) {
        log.info("Методом GET запрошен список вещей владельца с ид = {}", ownerId);
        Collection<ItemDto> items = itemService.findAllForUser(ownerId);
        log.info("Подготовлен ответ на GET /items по ид владельца = {} с телом: {}", ownerId, items);
        return ResponseEntity.ok().body(items);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> findOne(@PathVariable Long itemId) {
        log.info("Методом GET запрошена вещь по идентификатору {}", itemId);
        ItemDto itemDto = itemService.findById(itemId);
        log.info("Подготовлен ответ на GET /items/{} с телом: {}", itemId, itemDto);
        return ResponseEntity.ok().body(itemDto);
    }

    @PostMapping
    public ResponseEntity<ItemDto> create(@RequestHeader(USERID_HEADER) Long ownerId,
                                          @RequestBody ItemDto item) {
        log.info("Пришел POST запрос /items с телом: {}\nВ заголовке указан владелец с ид. {}",
                item, ownerId);
        final ItemDto newItem = itemService.create(ownerId, item);
        log.info("Подготовлен ответ на POST /items по ид владельца = {} с телом: {}", ownerId, newItem);
        return ResponseEntity.ok().body(newItem);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(@RequestHeader(USERID_HEADER) Long ownerId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto item) {
        log.info("Пришел PATCH запрос /items/{} с телом: {}\nВ заголовке указан владелец с ид. {}",
                itemId, item, ownerId);
        final ItemDto updatedItem = itemService.update(ownerId, itemId, item);
        log.info("Подготовлен ответ на PATCH /items/{} по ид владельца = {} с телом: {}",
                itemId, ownerId, updatedItem);
        return ResponseEntity.ok().body(updatedItem);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ItemDto> delete(@PathVariable Long itemId,
                                          @RequestHeader(USERID_HEADER) Long ownerId) {
        log.info("Пришел DELETE запрос /items/{}\nВ заголовке указан владелец с ид. {}",
                itemId, ownerId);
        final ItemDto deletedItem = itemService.delete(itemId, ownerId);
        log.info("Подготовлен ответ DELETE /items/{} по ид владельца = {} с телом: {}",
                itemId, ownerId, deletedItem);
        return ResponseEntity.ok().body(deletedItem);
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemDto>> searchItems(@RequestParam String text) {
        log.info("Методом GET /items/search?{} запрошен список вещей по тексту", text);
        Collection<ItemDto> itemsByText = itemService.searchByText(text);
        log.info("Отправлен ответ GET /items/search?{} с телом: {}", text, itemsByText);
        return ResponseEntity.ok().body(itemsByText);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long itemId,
                                                 @RequestBody CommentDto commentDto,
                                                 @RequestHeader(USERID_HEADER) Long userId) {
        log.info("Пришел POST запрос /items/{}/comment с телом: {}\nВ заголовке указан пользователь с ид. {}",
                itemId, commentDto, userId);
        CommentDto comment = itemService.addComment(itemId, commentDto, userId);
        log.info("Подготовлен ответ на POST /items/{}/comment по ид пользователя = {} с телом: {}",
                itemId, userId, comment);
        return ResponseEntity.ok().body(comment);
    }
}
