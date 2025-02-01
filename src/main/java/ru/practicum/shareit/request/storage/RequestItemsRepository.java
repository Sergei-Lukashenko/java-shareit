package ru.practicum.shareit.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.RequestItems;

import java.util.Collection;

public interface RequestItemsRepository extends JpaRepository<RequestItems, Long> {
    Collection<RequestItems> findByRequest(ItemRequest request);
}
