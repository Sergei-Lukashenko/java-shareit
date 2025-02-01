package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.model.RequestItems;

@Mapper
public interface RequestItemsMapper {
    RequestItemsMapper INSTANCE = Mappers.getMapper(RequestItemsMapper.class);

    RequestItems toRequestItems(RequestItemsDto requestItemsDto);

    @Mapping(target = "requestId", expression = "java(requestItems.getRequest().getId())")
    @Mapping(target = "itemId", expression = "java(requestItems.getItem().getId())")
    @Mapping(target = "itemName", expression = "java(requestItems.getItem().getName())")
    @Mapping(target = "ownerId", expression = "java(requestItems.getItem().getOwner().getId())")
    RequestItemsDto toRequestItemsDto(RequestItems requestItems);
}
