package ru.practicum.shareit.item.storage;


import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Collection<Item> findAllForUser(Long userId);

    Item findOneById(Long itemId);

    Item create(Long userId, Item item);

    Item update(Long userId, Long itemId, Item item);

    Item delete(Long itemId);

    boolean isItemExists(Long itemId);

    Collection<Item> searchByText(String text);
}