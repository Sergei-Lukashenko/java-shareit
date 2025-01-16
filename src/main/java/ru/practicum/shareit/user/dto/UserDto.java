package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;

    @Size(max = 255)
    private String name;

    @Email(message = "Некорректный email.")
    @Size(max = 255)
    private String email;
}