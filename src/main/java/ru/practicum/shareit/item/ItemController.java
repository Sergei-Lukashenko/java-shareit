package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
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
    public Collection<ItemDto> findAll(@RequestHeader(USERID_HEADER) Long userId) {
        log.info("Методом GET запрошен список вещей пользователя {}", userId);
        return itemService.findAllForUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findOne(@PathVariable Long itemId) {
        log.info("Методом GET запрошена вещь по идентификатору {}", itemId);
        return itemService.findById(itemId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(USERID_HEADER) Long userId, @RequestBody Item item) {
        log.info("Пришел POST запрос /items с телом: {}\nВ заголовке указан пользователь с ид. {}",
                item, userId);
        if (item.getName() == null || item.getName().isBlank()) {
            log.error("Получен запрос на создание вещи с пустым наименованием");
            throw new ConditionsNotMetException("Нельзя создавать вещь без наименования");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            log.error("Получен запрос на создание вещи с пустым описанием");
            throw new ConditionsNotMetException("Нельзя создавать вещь без описания");
        }
        if (item.getAvailable() == null) {
            log.error("Получен запрос на создание вещи с пустым полем available");
            throw new ConditionsNotMetException("Нельзя создавать вещь без указания ее доступности");
        }
        final ItemDto newItem = itemService.create(userId, item);
        log.info("Подготовлен ответ на POST /items с телом: {}", newItem);
        return newItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USERID_HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody Item item) {
        log.info("Пришел PATCH запрос /items/{} с телом: {}\nВ заголовке указан пользователь с ид. {}",
                itemId, item, userId);
        final ItemDto updatedItem = itemService.update(userId, itemId, item);
        log.info("Подготовлен ответ на PATCH /items/{} с телом: {}", itemId, updatedItem);
        return updatedItem;
    }

    @DeleteMapping("/{itemId}")
    public ItemDto delete(@PathVariable Long itemId) {
        log.info("Пришел DELETE запрос /items/{}", itemId);
        final ItemDto deletedItem = itemService.delete(itemId);
        log.info("Подготовлен ответ DELETE /items/{} с телом: {}", itemId, deletedItem);
        return deletedItem;
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text) {
        log.info("Методом GET /items/search?{} запрошен список вещей по тексту", text);
        Collection<ItemDto> itemsByText = itemService.searchByText(text);
        log.info("Отправлен ответ GET /items/search?{} с телом: {}", text, itemsByText);
        return itemsByText;
    }
}
