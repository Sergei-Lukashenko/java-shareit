package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> findAll() {
        log.info("Методом GET запрошен список пользователей");
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto findOne(@PathVariable Long userId) {
        log.info("Методом GET запрошен пользователь по идентификатору {}", userId);
        return userService.findById(userId);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        log.info("Пришел POST запрос /users с телом: {}", user);
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Получен запрос на создание пользователя с пустым eMail");
            throw new ConditionsNotMetException("Нельзя создавать пользователей без eMail");
        }
        final UserDto newUser = userService.create(UserMapper.toUser(user));
        log.info("Подготовлен ответ на POST /users с телом: {}", newUser);
        return newUser;
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                          @Valid @RequestBody UserDto user) {
        log.info("Пришел PATCH запрос /users/{} с телом: {}", userId, user);
        final UserDto updatedUser = userService.update(userId, UserMapper.toUser(user));
        log.info("Подготовлен ответ на PATCH /users/{} с телом: {}", userId, updatedUser);
        return updatedUser;
    }

    @DeleteMapping("/{userId}")
    public UserDto delete(@PathVariable Long userId) {
        log.info("Пришел DELETE запрос /users/{}", userId);
        final UserDto deletedUser = userService.delete(userId);
        log.info("Подготовлен ответ DELETE /users/{} с телом: {}", userId, deletedUser);
        return deletedUser;
    }
}
