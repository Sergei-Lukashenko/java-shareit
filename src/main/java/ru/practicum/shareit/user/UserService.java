package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserAlreadyExists;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String NOT_FOUND_BY_ID = "Пользователь по указанному идентификатору не найден";

    private final UserStorage userStorage;

    public Collection<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    public UserDto findById(Long userId) {
        if (!userStorage.isUserExists(userId)) {
            throw new NotFoundException(NOT_FOUND_BY_ID);
        }
        return UserMapper.toUserDto(userStorage.findOneById(userId));
    }

    public UserDto create(User user) {
        if (userStorage.isEmailExists(user.getEmail())) {
            throw new UserAlreadyExists("Такой email пользователя уже занят");
        }
        return UserMapper.toUserDto(userStorage.create(user));
    }

    public UserDto update(Long userId, User user) {
        if (!userStorage.isUserExists(userId)) {
            throw new NotFoundException(NOT_FOUND_BY_ID);
        }
        return UserMapper.toUserDto(userStorage.update(userId, user));
    }

    public UserDto delete(Long userId) {
        if (!userStorage.isUserExists(userId)) {
            throw new NotFoundException(NOT_FOUND_BY_ID);
        }
        return UserMapper.toUserDto(userStorage.delete(userId));
    }
}