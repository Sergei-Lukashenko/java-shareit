package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // searching by booker
    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    List<Booking> findAllByBookerIdAndStatusAndStartBeforeAndEndAfterOrderByStartDesc(
            Long bookerId, BookingStatus status, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStatusAndEndBeforeOrderByStartDesc(
            Long bookerId, BookingStatus status, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStatusAndStartAfterOrderByStartDesc(
            Long bookerId, BookingStatus status, LocalDateTime start);

    // searching by owner
    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    List<Booking> findAllByItemOwnerIdAndStatusAndStartBeforeAndEndAfterOrderByStartDesc(
            Long ownerId, BookingStatus status, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndStatusAndEndBeforeOrderByStartDesc(
            Long ownerId, BookingStatus status, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndStatusAndStartAfterOrderByStartDesc(
            Long ownerId, BookingStatus status, LocalDateTime start);

    // other, used internally
    List<Booking> findAllByBookerAndItemAndStatusAndEndBefore(User booker, Item item, BookingStatus status, LocalDateTime end);

    @Query(value = "SELECT * FROM bookings AS b " +
           "WHERE b.item_id = :itemId AND b.status = 'APPROVED' AND b.end_date < CURRENT_TIMESTAMP - INTERVAL '1 day'" +
           "ORDER BY b.end_date DESC FETCH FIRST 1 ROWS ONLY",
            nativeQuery = true)
    Booking findFirstApprovedByItemIdEndBeforeYesterdayOrderByEndDesc(@Param("itemId") Long itemId);

    @Query(value = "SELECT * FROM bookings AS b " +
            "WHERE b.item_id = :itemId AND b.status = 'APPROVED' AND b.start_date > CURRENT_TIMESTAMP " +
            "ORDER BY b.start_date FETCH FIRST 1 ROWS ONLY",
            nativeQuery = true)
    Booking findFirstApprovedByItemIdStartAfterCurrentMomentOrderByStartAsc(@Param("itemId") Long itemId);
}