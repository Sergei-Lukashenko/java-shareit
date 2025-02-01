package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;


@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @Valid @RequestBody ItemRequestDto dto) {
        dto.setUserId(userId);
        log.info("Создаем новый запрос от пользователя {}", userId);
        return requestClient.addRequest(dto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequestsByUser(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Получаем все запросы пользователя {}", userId);
        return requestClient.getAllRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Получаем вообще все запросы");
        return requestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @PathVariable Integer requestId) {
        log.info("Получаем информацию по запросу {}", requestId);
        return requestClient.getRequestById(requestId, userId);
    }
}
