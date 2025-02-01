package ru.practicum.shareit.request.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestedItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto);

    Collection<ItemRequestDto> findAllOwn(Long userId);

    Collection<ItemRequestDto> findAllOthers(Long userId);

    ItemRequestDto findById(Long requestId, Long userId);

    ItemRequest getReqById(Long requestId);

    RequestedItemsDto createForItem(Item item, ItemRequest itemRequest);
}
