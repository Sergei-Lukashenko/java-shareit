package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.model.RequestedItems;

@Mapper
public interface RequestedItemsMapper {
    RequestedItemsMapper INSTANCE = Mappers.getMapper(RequestedItemsMapper.class);

    RequestedItems toRequestItems(RequestedItemsDto requestedItemsDto);

    @Mapping(target = "requestId", expression = "java(requestedItems.getRequest().getId())")
    @Mapping(target = "itemId", expression = "java(requestedItems.getItem().getId())")
    @Mapping(target = "itemName", expression = "java(requestedItems.getItem().getName())")
    @Mapping(target = "ownerId", expression = "java(requestedItems.getItem().getOwner().getId())")
    RequestedItemsDto toRequestItemsDto(RequestedItems requestedItems);
}
