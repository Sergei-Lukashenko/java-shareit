package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Entity
@Table(name = "requested_items")
@Data
@Builder
@EqualsAndHashCode(of = { "id" })
@AllArgsConstructor
@NoArgsConstructor
public class RequestedItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Item.class)
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = ItemRequest.class)
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    @Column
    private LocalDateTime created;
}
