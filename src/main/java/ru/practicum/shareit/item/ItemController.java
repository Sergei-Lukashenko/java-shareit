package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Collection<ItemDto>> findAll(@RequestHeader(USERID_HEADER) Long userId) {
        log.info("Методом GET запрошен список вещей пользователя с ид = {}", userId);
        Collection<ItemDto> items = itemService.findAllForUser(userId);
        log.info("Подготовлен ответ на GET /items по ид пользователя = {} с телом: {}", userId, items);
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
    public ResponseEntity<ItemDto> create(@RequestHeader(USERID_HEADER) Long userId, @RequestBody ItemDto item) {
        log.info("Пришел POST запрос /items с телом: {}\nВ заголовке указан пользователь с ид. {}",
                item, userId);
        final ItemDto newItem = itemService.create(userId, item);
        log.info("Подготовлен ответ на POST /items по ид пользователя = {} с телом: {}", userId, newItem);
        return ResponseEntity.ok().body(newItem);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(@RequestHeader(USERID_HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto item) {
        log.info("Пришел PATCH запрос /items/{} с телом: {}\nВ заголовке указан пользователь с ид. {}",
                itemId, item, userId);
        final ItemDto updatedItem = itemService.update(userId, itemId, item);
        log.info("Подготовлен ответ на PATCH /items/{} по ид пользователя = {} с телом: {}", itemId, userId, updatedItem);
        return ResponseEntity.ok().body(updatedItem);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ItemDto> delete(@PathVariable Long itemId) {
        log.info("Пришел DELETE запрос /items/{}", itemId);
        final ItemDto deletedItem = itemService.delete(itemId);
        log.info("Подготовлен ответ DELETE /items/{} с телом: {}", itemId, deletedItem);
        return ResponseEntity.ok().body(deletedItem);
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemDto>> searchItems(@RequestParam String text) {
        log.info("Методом GET /items/search?{} запрошен список вещей по тексту", text);
        Collection<ItemDto> itemsByText = itemService.searchByText(text);
        log.info("Отправлен ответ GET /items/search?{} с телом: {}", text, itemsByText);
        return ResponseEntity.ok().body(itemsByText);
    }
}
