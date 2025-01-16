package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.StateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserAccessViolationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private static final String BOOKING_NOT_FOUND = "Бронирование по данному идентификатору не найдено";
    private static final String BOOKING_NOT_ACCESSIBLE = "У пользователя нет доступа к данному бронированию";

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    private final UserService userService;

    @Override
    public BookingDto findById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(BOOKING_NOT_FOUND));

        final Long bookerId = booking.getBooker().getId();
        final Long ownerIdOfBookingItem = booking.getItem().getOwner().getId();
        if (Objects.equals(bookerId, userId) || Objects.equals(ownerIdOfBookingItem, userId)) {
            return BookingMapper.INSTANCE.toBookingDto(booking);
        } else {
            throw new UserAccessViolationException(BOOKING_NOT_ACCESSIBLE);
        }
    }

    @Override
    public Collection<BookingDto> findByBookerId(Long bookerId, StateDto state) {
        List<Booking> result = Collections.emptyList();

        switch (state) {
            case ALL -> result = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);

            case WAITING -> result = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                    bookerId, BookingStatus.WAITING);

            case REJECTED -> result = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                    bookerId, BookingStatus.REJECTED);

            case CURRENT -> result = bookingRepository.findAllByBookerIdAndStatusAndStartBeforeAndEndAfterOrderByStartDesc(
                    bookerId, BookingStatus.APPROVED, LocalDateTime.now(), LocalDateTime.now());

            case PAST -> result = bookingRepository.findAllByBookerIdAndStatusAndEndBeforeOrderByStartDesc(
                    bookerId, BookingStatus.APPROVED, LocalDateTime.now());

            case FUTURE -> result = bookingRepository.findAllByBookerIdAndStatusAndStartAfterOrderByStartDesc(
                    bookerId, BookingStatus.APPROVED, LocalDateTime.now());
        }

        return result.stream()
                .map(BookingMapper.INSTANCE::toBookingDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> findByOwnerId(Long ownerId, StateDto state) {
        userService.findById(ownerId);   // Check for user existence ignoring the returned value

        List<Booking> result = Collections.emptyList();

        switch (state) {
            case ALL -> result = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId);

            case WAITING -> result = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                    ownerId, BookingStatus.WAITING);

            case REJECTED -> result = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                    ownerId, BookingStatus.REJECTED);

            case CURRENT -> result = bookingRepository.findAllByItemOwnerIdAndStatusAndStartBeforeAndEndAfterOrderByStartDesc(
                    ownerId, BookingStatus.APPROVED, LocalDateTime.now(), LocalDateTime.now());

            case PAST -> result = bookingRepository.findAllByItemOwnerIdAndStatusAndEndBeforeOrderByStartDesc(
                    ownerId, BookingStatus.APPROVED, LocalDateTime.now());

            case FUTURE -> result = bookingRepository.findAllByItemOwnerIdAndStatusAndStartAfterOrderByStartDesc(
                    ownerId, BookingStatus.APPROVED, LocalDateTime.now());
        }

        return result.stream()
                .map(BookingMapper.INSTANCE::toBookingDto)
                .toList();
    }

    @Override
    @Transactional
    public BookingDto create(Long bookerId, BookingDto bookingDto) {
        User user = UserMapper.INSTANCE.toUser(userService.findById(bookerId));
        final Long itemId = bookingDto.getItemId();
        if (itemId == null) {
            throw new NotFoundException(BOOKING_NOT_ACCESSIBLE + ". Указан пустой ид. вещи в запросе на бронирование");
        }

        Booking booking = BookingMapper.INSTANCE.toBooking(bookingDto);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(BOOKING_NOT_ACCESSIBLE + ". Указанная вещь не найдена"));

        if (!item.getAvailable()) {
            throw new ConditionsNotMetException(BOOKING_NOT_ACCESSIBLE + ". Доступность отключена владельцем");
        }

        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);

        return BookingMapper.INSTANCE.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto approve(Long ownerId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(BOOKING_NOT_FOUND));

        final Long itemOwnerId = booking.getItem().getOwner().getId();
        if (!Objects.equals(itemOwnerId, ownerId)) {
            throw new UserAccessViolationException(BOOKING_NOT_ACCESSIBLE);
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.INSTANCE.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public Collection<BookingDto> findByItemAndBooker(Item item, User booker) {
        List<Booking> result = bookingRepository.findAllByBookerAndItemAndStatusAndEndBefore(booker,
                item,
                BookingStatus.APPROVED,
                LocalDateTime.now());

        return result.stream()
                .map(BookingMapper.INSTANCE::toBookingDto)
                .toList();
    }

    @Override
    public Optional<Booking> findLastBooking(Item item) {
        Booking lastBooking = bookingRepository.findFirstApprovedByItemIdEndBeforeYesterdayOrderByEndDesc(
                item.getId());
        if (lastBooking != null) {
            return Optional.of(lastBooking);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Booking> findNextBooking(Item item) {
        Booking nextBooking = bookingRepository.findFirstApprovedByItemIdStartAfterCurrentMomentOrderByStartAsc(
                item.getId());
        if (nextBooking != null) {
            return Optional.of(nextBooking);
        } else {
            return Optional.empty();
        }
    }
}
