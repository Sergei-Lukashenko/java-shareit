package ru.practicum.shareit.request.dto;

import lombok.Data;

@Data
public class ItemResponse {
    private Integer itemId;
    private String itemName;
    private Integer ownerId;
}
