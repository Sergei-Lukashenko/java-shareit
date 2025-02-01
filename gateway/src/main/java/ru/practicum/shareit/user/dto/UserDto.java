package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    private Integer id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @NotNull
    @Email(message = "Некорректный email.")
    @Size(max = 255)
    private String email;
}
