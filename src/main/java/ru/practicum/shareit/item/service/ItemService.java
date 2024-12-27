package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> findAllForUser(Long userId);

    ItemDto findById(Long itemId);

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemDto delete(Long itemId);

    Collection<ItemDto> searchByText(String text);
}
