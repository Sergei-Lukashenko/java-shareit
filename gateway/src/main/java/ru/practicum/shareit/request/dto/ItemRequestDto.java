package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;


@Data
public class ItemRequestDto {
    private Integer id;

    @NotNull
    @Size(max = 512)
    private String description;

    private LocalDateTime created;

    private Integer userId;

    private Collection<ItemResponse> items;
}
