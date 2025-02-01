package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserClient userClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получаем всех пользователей");
        return userClient.getUsers();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserById(@PathVariable Integer userId) {
        log.info("Получаем пользователя с ID = {}", userId);
        return userClient.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto dto) {
        log.info("Создаем нового пользователя");
        return userClient.createUser(dto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Integer userId, @RequestBody UserDto dto) {
        log.info("Обновляем пользователя с ID = {}", userId);
        return userClient.updateUser(userId, dto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        log.info("Удаляем пользователя с ID = {}", userId);
        userClient.deleteUser(userId);
    }


/*
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer userId) {
        log.info("Получаем пользователя с ID = {}", userId);
        return ResponseEntity.ok().body(userClient.getUserById(userId));
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получаем всех пользователей");
        return ResponseEntity.ok().body(userClient.getUsers());
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto dto) {
        log.info("Создаем нового пользователя");
        return ResponseEntity.ok().body(userClient.createUser(dto));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Integer userId, @RequestBody UserDto dto) {
        log.info("Обновляем пользователя с ID = {}", userId);
        return ResponseEntity.ok().body(userClient.updateUser(userId, dto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Integer userId) {
        log.info("Удаляем пользователя с ID = {}", userId);
        return ResponseEntity.ok().body(userClient.deleteUser(userId));
    }
*/
}
