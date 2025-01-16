package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long userId);

    Optional<Item> findByIdAndOwnerId(Long id, Long ownerId);

    @Query("SELECT it FROM Item it " +
            "WHERE (LOWER(it.name) LIKE LOWER(CONCAT('%', :text, '%') ) OR " +
            "LOWER(it.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND it.available = true")
    List<Item> searchByText(@Param("text") String text);
}