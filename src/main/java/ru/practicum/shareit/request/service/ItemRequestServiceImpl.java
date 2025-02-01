package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.RequestedItemsDto;
import ru.practicum.shareit.request.dto.RequestedItemsMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.RequestedItems;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.request.storage.RequestItemsRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final RequestItemsRepository requestItemsRepository;

    @Override
    @Transactional
    public ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto) {
        userService.findById(userId);   // NotFoundException if no user found

        ItemRequest itemRequest = ItemRequestMapper.INSTANCE.toItemRequest(itemRequestDto);
        itemRequest.setUserId(userId);
        itemRequest.setCreated(LocalDateTime.now());

        itemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.INSTANCE.toItemRequestDto(itemRequest);
    }

    @Override
    public Collection<ItemRequestDto> findAllOwn(Long userId) {
        userService.findById(userId);   // NotFoundException if no user found

        return itemRequestRepository.findByUserIdOrderByCreatedDesc(userId).stream()
                .map(this::loadRequestItems)
                .toList();
    }

    @Override
    public Collection<ItemRequestDto> findAllOthers(Long userId) {
        userService.findById(userId);   // NotFoundException if no user found

        return itemRequestRepository.findByUserIdNotOrderByCreatedDesc(userId).stream()
                .map(ItemRequestMapper.INSTANCE::toItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestDto findById(Long requestId, Long userId) {
        userService.findById(userId);   // NotFoundException if no user found

        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Запрос вещи с ид. запроса %d не найден", requestId))
                );
        return loadRequestItems(itemRequest);
    }

    @Override
    public ItemRequest getReqById(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Запрос вещи с ид. запроса %d не найден", requestId))
                );
    }

    @Override
    @Transactional
    public RequestedItemsDto createForItem(Item item, ItemRequest itemRequest) {
        RequestedItems requestedItems = RequestedItems.builder()
                .item(item)
                .request(itemRequest)
                .created(LocalDateTime.now())
                .build();

        requestedItems = requestItemsRepository.save(requestedItems);

        return RequestedItemsMapper.INSTANCE.toRequestItemsDto(requestedItems);
    }

    @Transactional
    private ItemRequestDto loadRequestItems(ItemRequest itemRequest) {
        Collection<RequestedItems> requestedItems = requestItemsRepository.findByRequest(itemRequest);

        List<ItemDto> itemDtos = requestedItems.stream()
                .map(RequestedItems::getItem)
                .map(ItemMapper.INSTANCE::toItemDto)
                .toList();

        ItemRequestDto itemRequestDto = ItemRequestMapper.INSTANCE.toItemRequestDto(itemRequest);

        itemRequestDto.setItems(itemDtos);

        return itemRequestDto;
    }

}
