package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Collection<UserDto>> findAll() {
        log.info("Методом GET запрошен список пользователей");
        Collection<UserDto> users = userService.findAll();
        log.info("Подготовлен ответ на GET /users с телом: {}", users);
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findOne(@PathVariable Long userId) {
        log.info("Методом GET запрошен пользователь по идентификатору {}", userId);
        UserDto userDto = userService.findById(userId);
        log.info("Подготовлен ответ на GET /users/{} с телом: {}", userId, userDto);
        return ResponseEntity.ok().body(userDto);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto user) {
        log.info("Пришел POST запрос /users с телом: {}", user);
        final UserDto newUser = userService.create(user);
        log.info("Подготовлен ответ на POST /users с телом: {}", newUser);
        return ResponseEntity.ok().body(newUser);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> update(@PathVariable Long userId,
                          @Valid @RequestBody UserDto user) {
        log.info("Пришел PATCH запрос /users/{} с телом: {}", userId, user);
        final UserDto updatedUser = userService.update(userId, user);
        log.info("Подготовлен ответ на PATCH /users/{} с телом: {}", userId, updatedUser);
        return ResponseEntity.ok().body(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserDto> delete(@PathVariable Long userId) {
        log.info("Пришел DELETE запрос /users/{}", userId);
        final UserDto deletedUser = userService.delete(userId);
        log.info("Подготовлен ответ DELETE /users/{} с телом: {}", userId, deletedUser);
        return ResponseEntity.ok().body(deletedUser);
    }
}
