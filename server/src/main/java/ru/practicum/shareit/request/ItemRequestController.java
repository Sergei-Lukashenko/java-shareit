package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String USERID_HEADER = "X-Sharer-User-Id";

    private final ItemRequestService requestService;

    @PostMapping
    public ResponseEntity<ItemRequestDto> create(@RequestHeader(USERID_HEADER) Long requesterId,
                                                 @RequestBody ItemRequestDto itemRequest) {
        log.info("Пришел POST запрос /requests с телом: {}\nВ заголовке указан ид. инициатора запроса {}",
                itemRequest, requesterId);
        ItemRequestDto request = requestService.create(requesterId, itemRequest);

        log.info("Подготовлен ответ на POST /requests по ид. инициатора = {} с телом: {}", requesterId, request);
        return ResponseEntity.ok().body(request);
    }

    @GetMapping
    public ResponseEntity<Collection<ItemRequestDto>> findAllOwn(
            @RequestHeader(USERID_HEADER) Long requesterId) {

        log.info("Получен GET для списка запросов вещей инициатора с ид = {}", requesterId);
        Collection<ItemRequestDto> requestDtos = requestService.findAllOwn(requesterId);

        log.info("Подготовлен ответ на GET /requests по ид. инициатора = {} с телом: {}", requesterId, requestDtos);
        return ResponseEntity.ok().body(requestDtos);
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<ItemRequestDto>> findAllOthers(
            @RequestHeader(USERID_HEADER) Long userId) {

        log.info("Получен GET для списка запросов вещей всех инициатора кроме ид = {}", userId);
        Collection<ItemRequestDto> requestDtos = requestService.findAllOthers(userId);

        log.info("Подготовлен ответ на GET /requests/all дли ид. = {} с телом: {}", userId, requestDtos);
        return ResponseEntity.ok().body(requestDtos);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> findOne(
            @RequestHeader(USERID_HEADER) Long userId,
            @PathVariable Long requestId) {

        log.info("Получен GET для поиска запроcа вещи по идентификатору {} от пользователя {}", requestId, userId);
        ItemRequestDto requestDto = requestService.findById(requestId, userId);

        log.info("Подготовлен ответ на GET /requests/{} с телом: {}", requestId, requestDto);
        return ResponseEntity.ok().body(requestDto);
    }

}
