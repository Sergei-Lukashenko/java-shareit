package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(of = { "id" })
public class Item {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private @NotNull Boolean available;
}