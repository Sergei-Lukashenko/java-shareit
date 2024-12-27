package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final String ITEM_NOT_FOUND_BY_ID = "Вещь по указанному идентификатору не найдена";
    private static final String USER_NOT_FOUND_BY_ID = "Пользователь по указанному идентификатору не найден";

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public Collection<ItemDto> findAllForUser(Long userId) {
        return itemStorage.findAllForUser(userId).stream()
                .map(ItemMapper.INSTANCE::toItemDto)
                .toList();
    }

    @Override
    public ItemDto findById(Long itemId) {
        if (!itemStorage.isItemExists(itemId)) {
            throw new NotFoundException(ITEM_NOT_FOUND_BY_ID);
        }
        return ItemMapper.INSTANCE.toItemDto(itemStorage.findOneById(itemId));
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            log.error("Получен запрос на создание вещи с пустым наименованием");
            throw new ConditionsNotMetException("Нельзя создавать вещь без наименования");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            log.error("Получен запрос на создание вещи с пустым описанием");
            throw new ConditionsNotMetException("Нельзя создавать вещь без описания");
        }
        if (itemDto.getAvailable() == null) {
            log.error("Получен запрос на создание вещи с пустым полем available");
            throw new ConditionsNotMetException("Нельзя создавать вещь без указания ее доступности");
        }
        if (!userStorage.isUserExists(userId)) {
            throw new NotFoundException(USER_NOT_FOUND_BY_ID);
        }
        Item item = ItemMapper.INSTANCE.toItem(itemDto);
        return ItemMapper.INSTANCE.toItemDto(itemStorage.create(userId, item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        if (!itemStorage.isItemExists(itemId)) {
            throw new NotFoundException(ITEM_NOT_FOUND_BY_ID);
        }
        Item item = ItemMapper.INSTANCE.toItem(itemDto);
        return ItemMapper.INSTANCE.toItemDto(itemStorage.update(userId, itemId, item));
    }

    @Override
    public ItemDto delete(Long itemId) {
        if (!itemStorage.isItemExists(itemId)) {
            throw new NotFoundException(ITEM_NOT_FOUND_BY_ID);
        }
        return ItemMapper.INSTANCE.toItemDto(itemStorage.delete(itemId));
    }

    @Override
    public Collection<ItemDto> searchByText(String text) {
        return itemStorage.searchByText(text).stream()
                .map(ItemMapper.INSTANCE::toItemDto)
                .toList();
    }
}
