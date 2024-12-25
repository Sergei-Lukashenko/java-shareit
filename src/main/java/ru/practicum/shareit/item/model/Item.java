package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.user.User;

@Data
@Builder
@EqualsAndHashCode(of = { "id" })
public class Item {
    private Long id;
    private String name;
    private String description;
    private @NotNull Boolean available;
    private User owner;
    private Long request;  // ??? ссылка на объект ItemRequest, который появится позже
}