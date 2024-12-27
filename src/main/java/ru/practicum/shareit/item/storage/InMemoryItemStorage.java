package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.*;

@Primary
@Repository
@Slf4j
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private final UserStorage userStorage;
    private final Map<Long, Item> items = new HashMap<>();

    private Long maxId = 0L;

    @Override
    public Collection<Item> findAllForUser(final Long userId) {
        return items.values().stream()
                .filter(it -> Objects.equals(it.getUserId(), userId))
                .toList();
    }

    @Override
    public Item findOneById(final Long itemId) {
        return items.get(itemId);
    }

    @Override
    public Item create(final Long userId, final Item item) {
        item.setId(++maxId);
        item.setUserId(userId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(final Long userId, final Long itemId, final Item item) {
        Item updatedItem = findOneById(itemId);

        if (!Objects.equals(updatedItem.getUserId(), userId)) {
            log.error("Пользователь с ид = {} пытается обновить вещь пользователя с ид = {}",
                    userId, updatedItem.getUserId());
            throw new NotFoundException("Пользователь не является владельцем указанной вещи");
        }

        return ItemValidator.mergeForUpdate(updatedItem, item);
    }

    @Override
    public Item delete(final Long itemId) {
        return items.remove(itemId);
    }

    @Override
    public boolean isItemExists(Long itemId) {
        return items.containsKey(itemId);
    }

    @Override
    public Collection<Item> searchByText(final String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        String t = text.toLowerCase();
        return items.values().stream()
                .filter(it -> it.getAvailable() != null && it.getAvailable() &&
                    ((it.getName() != null && it.getName().toLowerCase().contains(t))
                            || (it.getDescription() != null && it.getDescription().toLowerCase().contains(t))
                    )
                )
                .toList();
    }
}
