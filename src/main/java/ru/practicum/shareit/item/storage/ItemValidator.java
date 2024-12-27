package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

public class ItemValidator {
    public static Item mergeForUpdate(Item updatedItem, Item newItem) {
        if (newItem.getName() != null) {
            updatedItem.setName(newItem.getName());
        }
        if (newItem.getDescription() != null) {
            updatedItem.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            updatedItem.setAvailable(newItem.getAvailable());
        }
        return updatedItem;
    }

}
