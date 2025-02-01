package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface BookingService {
    BookingDto findById(Long userId, Long bookingId);

    Collection<BookingDto> findByBookerId(Long bookerId, StateDto state);

    Collection<BookingDto> findByOwnerId(Long ownerId, StateDto state);

    BookingDto create(Long bookerId, BookingDto bookingDto);

    BookingDto approve(Long ownerId, Long bookingId, Boolean approved);

    Collection<BookingDto> findByItemAndBooker(Item item, User booker);

    Optional<Booking> findLastBooking(Item item);

    Optional<Booking> findNextBooking(Item item);
}