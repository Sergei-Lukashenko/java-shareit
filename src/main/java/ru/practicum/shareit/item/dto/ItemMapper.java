package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner().getId())
                .request(item.getRequest())  // ??? потом замениим на: item.getRequest() != null ? item.getRequest().getId() : null
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                // ??? будет что-то такое: owner(itemDto.getOwner() == null ? null
                // : userService.getUser(itemDto.getOwner())  ???
                .request(itemDto.getRequest())  // ??? заменить на создание ItemRequest в будущем
                .build();
    }
}