package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final String ITEM_NOT_FOUND_BY_ID = "Вещь по указанному идентификатору не найдена";
    private static final String USER_NOT_FOUND_BY_ID = "Пользователь по указанному идентификатору не найден";

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public Collection<ItemDto> findAllForUser(Long userId) {
        return itemStorage.findAllForUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto findById(Long itemId) {
        if (!itemStorage.isItemExists(itemId)) {
            throw new NotFoundException(ITEM_NOT_FOUND_BY_ID);
        }
        return ItemMapper.toItemDto(itemStorage.findOneById(itemId));
    }

    @Override
    public ItemDto create(Long userId, Item item) {
        if (!userStorage.isUserExists(userId)) {
            throw new NotFoundException(USER_NOT_FOUND_BY_ID);
        }
        return ItemMapper.toItemDto(itemStorage.create(userId, item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, Item item) {
        if (!itemStorage.isItemExists(itemId)) {
            throw new NotFoundException(ITEM_NOT_FOUND_BY_ID);
        }
        return ItemMapper.toItemDto(itemStorage.update(userId, itemId, item));
    }

    @Override
    public ItemDto delete(Long itemId) {
        if (!itemStorage.isItemExists(itemId)) {
            throw new NotFoundException(ITEM_NOT_FOUND_BY_ID);
        }
        return ItemMapper.toItemDto(itemStorage.delete(itemId));
    }

    @Override
    public Collection<ItemDto> searchByText(String text) {
        return itemStorage.searchByText(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
