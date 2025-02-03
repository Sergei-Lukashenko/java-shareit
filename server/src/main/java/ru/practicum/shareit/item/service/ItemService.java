package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> findAllForUser(Long ownerId);

    ItemDto findById(Long itemId);

    ItemDto create(Long ownerId, ItemDto itemDto);

    ItemDto update(Long ownerId, Long itemId, ItemDto itemDto);

    ItemDto delete(Long itemId, Long ownerId);

    Collection<ItemDto> searchByText(String text);

    CommentDto addComment(Long itemId, CommentDto commentDto, Long userId);
}
