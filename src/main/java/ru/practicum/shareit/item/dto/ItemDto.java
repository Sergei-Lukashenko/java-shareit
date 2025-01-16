package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.Size;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

@Data
@Builder
public class ItemDto {
    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 512)
    private String description;

    private Boolean available;

    BookingDto lastBooking;

    BookingDto nextBooking;

    Collection<CommentDto> comments;
}