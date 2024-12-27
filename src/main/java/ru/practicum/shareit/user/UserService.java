package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private static final String NOT_FOUND_BY_ID = "Пользователь по указанному идентификатору не найден";

    private final UserStorage userStorage;

    public Collection<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(UserMapper.INSTANCE::toUserDto)
                .toList();
    }

    public UserDto findById(Long userId) {
        checkUserExistence(userId);
        return UserMapper.INSTANCE.toUserDto(userStorage.findOneById(userId));
    }

    public UserDto create(UserDto userDto) {
        checkEmail(userDto.getEmail());
        final User user = UserMapper.INSTANCE.toUser(userDto);
        return UserMapper.INSTANCE.toUserDto(userStorage.create(user));
    }

    public UserDto update(Long userId, UserDto userDto) {
        checkUserExistence(userId);
        checkEmail(userDto.getEmail(), userId);
        User user = userStorage.findOneById(userId);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        return UserMapper.INSTANCE.toUserDto(userStorage.update(userId, user));
    }

    public UserDto delete(Long userId) {
        if (!userStorage.isUserExists(userId)) {
            throw new NotFoundException(NOT_FOUND_BY_ID);
        }
        return UserMapper.INSTANCE.toUserDto(userStorage.delete(userId));
    }

    private void checkUserExistence(Long userId) {
        if (!userStorage.isUserExists(userId)) {
            log.error("Не найден пользователь по идентификатору {}", userId);
            throw new NotFoundException(NOT_FOUND_BY_ID);
        }
    }

    private void checkEmail(String email) {
        checkEmail(email, 0L);
    }

    private void checkEmail(String email, Long userId) {
        if (userId == 0L && (email == null || email.isBlank())) {   // при добавлении email не должен быть пустым
            log.error("Получен запрос на создание пользователя с пустым eMail");
            throw new ConditionsNotMetException("Нельзя создавать пользователей без eMail");
        }
        long duplicateUserId = userStorage.userIdHavingEmail(email);
        if (duplicateUserId > 0L && duplicateUserId != userId) {
            log.error("Уже имеется пользователь ид = {} c таким же eMail {},", duplicateUserId, email);
            throw new UserAlreadyExistsException("Такой email пользователя уже занят");
        }
    }
}