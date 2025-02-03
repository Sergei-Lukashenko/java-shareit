package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
	private long itemId;

	@NotNull(message = "Дата начала бронирования не может быть пустой")
	@FutureOrPresent(message = "Дата начала бронирования не может быть в прошлом")
	private LocalDateTime start;

	@NotNull(message = "Дата конца бронирования не может быть пустой")
	@Future(message = "Дата конца бронирования должна быть в будущем")
	private LocalDateTime end;
}
