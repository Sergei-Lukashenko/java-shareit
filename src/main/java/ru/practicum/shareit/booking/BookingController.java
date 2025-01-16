package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StateDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String USERID_HEADER = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> findById(@RequestHeader(USERID_HEADER) Long userId,
                                               @PathVariable Long bookingId) {

        log.info("Методом GET запрошено бронирование по идентификатору {} от пользователя {}",
                bookingId, userId);
        BookingDto booking = bookingService.findById(userId, bookingId);
        log.info("Подготовлен ответ на GET /bookings/{} от пользователя {} с телом: {}",
                bookingId, userId, booking);

        return ResponseEntity.ok().body(booking);
    }

    @GetMapping
    public ResponseEntity<Collection<BookingDto>> findAllByUserId(
            @RequestHeader(USERID_HEADER) Long userId,
            @RequestParam(defaultValue = "ALL", required = false) StateDto state) {

        log.info("Методом GET запрошен список бронирований пользователя с ид = {} и параметром состояния {}",
                userId, state);
        Collection<BookingDto> bookings = bookingService.findByBookerId(userId, state);
        log.info("Подготовлен ответ на GET /bookings?state={} с телом: {}", state, bookings);

        return ResponseEntity.ok().body(bookings);
    }

    @GetMapping("/owner")
    public ResponseEntity<Collection<BookingDto>> findAllByOwner(
            @RequestHeader(USERID_HEADER) Long userId,
            @RequestParam(defaultValue = "ALL", required = false) StateDto state) {

        log.info("Методом GET запрошен список бронирований вещей владельца с ид = {} и параметром состояния {}",
                userId, state);
        Collection<BookingDto> bookings = bookingService.findByOwnerId(userId, state);
        log.info("Подготовлен ответ на GET /bookings/owner?state={} с телом: {}", state, bookings);

        return ResponseEntity.ok().body(bookings);
    }

    @PostMapping
    public ResponseEntity<BookingDto> create(@RequestHeader(USERID_HEADER) Long userId,
                                             @Valid @RequestBody BookingDto bookingDto) {
        log.info("Пришел POST запрос /bookings с телом: {}\nВ заголовке указан пользователь с ид. {}",
                bookingDto, userId);
        BookingDto booking = bookingService.create(userId, bookingDto);
        log.info("Подготовлен ответ на POST /bookings по ид пользователя = {} с телом: {}", userId, bookingDto);

        return ResponseEntity.ok().body(booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approve(@RequestHeader(USERID_HEADER) Long userId,
                                              @RequestParam(required = true) Boolean approved,
                                              @PathVariable Long bookingId) {
        log.info("Пришел PATCH запрос /bookings/{}?approved={}\nВ заголовке указан владелец с ид. {}",
                bookingId, approved, userId);
        BookingDto booking = bookingService.approve(userId, bookingId, approved);
        log.info("Подготовлен ответ на PATCH /bookings/{}/approved={} по ид владельца = {} с телом: {}",
                bookingId, approved, userId, booking);

        return ResponseEntity.ok().body(booking);
    }
}
