package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestForComment;


@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllItemsById(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        log.info("Получаем все вещи пользователя {}", ownerId);
        return itemClient.getItemsByOwner(ownerId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Integer ownerId, @PathVariable Integer itemId) {
        log.info("Получаем данные вещи {} для пользователя {}", itemId, ownerId);
        return itemClient.getItemById(ownerId, itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Integer ownerId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Пробуем создать новую вещь для пользователя {}", ownerId);
        return itemClient.create(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                          @PathVariable Integer itemId,
                          @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        log.info("Обновляем вещь {}", itemId);
        return itemClient.update(ownerId, itemDto);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> searchForItem(@RequestParam String text) {
        log.info("Ищем по тексту {}", text);
        return itemClient.searchForItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> newComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable Integer itemId,
                                 @RequestBody RequestForComment text) {
        log.info("Создаем новый комментарий от пользователя {} на вещь {}", userId, itemId);
        return itemClient.addComment(userId, itemId, text);
    }
}