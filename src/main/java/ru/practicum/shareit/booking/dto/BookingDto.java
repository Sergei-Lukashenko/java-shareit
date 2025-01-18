package ru.practicum.shareit.booking.dto;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;

    @NotNull(message = "Дата начала бронирования не может быть пустой")
    @FutureOrPresent(message = "Дата начала бронирования не может быть в прошлом")
    private LocalDateTime start;

    @NotNull(message = "Дата конца бронирования не может быть пустой")
    @Future(message = "Дата конца бронирования должна быть в будущем")
    private LocalDateTime end;

    @Nullable
    private Long itemId;

    private ItemDto item;

    private UserDto booker;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
