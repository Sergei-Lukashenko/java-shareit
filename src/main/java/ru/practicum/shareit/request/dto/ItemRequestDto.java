package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestDto {
    private Long id;

    @Size(max = 512)
    private String description;

    private LocalDateTime created;

    private List<ItemDto> items;

    private Long userId;
}
