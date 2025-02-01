package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestItemsDto {
    private Long id;

    private Long requestId;

    private Long itemId;

    private String itemName;

    private Long ownerId;

    private LocalDateTime created;
}
