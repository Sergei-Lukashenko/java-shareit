package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final String ITEM_NOT_FOUND_BY_ID = "Вещь по указанному идентификатору не найдена";

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    private final UserService userService;
    private final BookingService bookingService;

    @Override
    public Collection<ItemDto> findAllForUser(Long ownerId) {
        return itemRepository.findByOwnerId(ownerId).stream()
                .map(this::loadDetails)
                .toList();
    }

    @Override
    public ItemDto findById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND_BY_ID));
        return loadDetails(item);
    }

    @Override
    @Transactional
    public ItemDto create(Long ownerId, ItemDto itemDto) {
        final String name = itemDto.getName();
        if (name == null || name.isEmpty() || name.isBlank()) {
            log.error("Получен запрос на создание вещи с пустым наименованием");
            throw new ConditionsNotMetException("Нельзя создавать вещь без наименования");
        }
        final String description = itemDto.getDescription();
        if (description == null || description.isEmpty() || description.isBlank()) {
            log.error("Получен запрос на создание вещи с пустым описанием");
            throw new ConditionsNotMetException("Нельзя создавать вещь без описания");
        }
        if (itemDto.getAvailable() == null) {
            log.error("Получен запрос на создание вещи с пустым полем available");
            throw new ConditionsNotMetException("Нельзя создавать вещь без указания ее доступности");
        }

        User owner = UserMapper.INSTANCE.toUser(userService.findById(ownerId));
        Item item = ItemMapper.INSTANCE.toItem(itemDto);
        item.setOwner(owner);
        return ItemMapper.INSTANCE.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(Long ownerId, Long itemId, ItemDto itemDto) {
        Item item = itemRepository.findByIdAndOwnerId(itemId, ownerId)
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND_BY_ID));

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.INSTANCE.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto delete(Long itemId, Long ownerId) {
        Item item = itemRepository.findByIdAndOwnerId(itemId, ownerId)
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND_BY_ID));

        itemRepository.deleteById(itemId);

        return ItemMapper.INSTANCE.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> searchByText(String text) {
        if (text == null || text.isEmpty() || text.isBlank()) {
            return Collections.emptyList();
        } else {
            return itemRepository.searchByText(text).stream()
                    .map(ItemMapper.INSTANCE::toItemDto)
                    .toList();
        }
    }

    @Override
    @Transactional
    public CommentDto addComment(Long itemId, CommentDto commentDto, Long userId) {
        User author = UserMapper.INSTANCE.toUser(userService.findById(userId));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND_BY_ID));

        Collection<BookingDto> bookingDtos = bookingService.findByItemAndBooker(item, author);

        if (bookingDtos.isEmpty()) {
            log.error("Получен запрос на добавление комментария к вещи {}, но она не забронирована для пользователя {}",
                    itemId, userId);
            throw new ConditionsNotMetException("У пользователя нет бронирований по указанной вещи");
        }

        Comment comment = CommentMapper.INSTANCE.toComment(commentDto);

        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        comment = commentRepository.save(comment);

        return CommentMapper.INSTANCE.toCommentDto(comment);
    }

    @Transactional
    private ItemDto loadDetails(Item item) {
        Collection<Comment> comments = commentRepository.findAllByItem(item);

        ItemDto itemDto = ItemMapper.INSTANCE.toItemDto(item);

        if (comments == null) {
            itemDto.setComments(Collections.emptyList());
        } else {
            itemDto.setComments(comments.stream()
                    .map(CommentMapper.INSTANCE::toCommentDto)
                    .toList());
        }

        bookingService.findLastBooking(item)
                .ifPresent(booking -> itemDto.setLastBooking(BookingMapper.INSTANCE.toBookingDto(booking)));

        bookingService.findNextBooking(item)
                .ifPresent(booking -> itemDto.setNextBooking(BookingMapper.INSTANCE.toBookingDto(booking)));

        return itemDto;
    }

}
