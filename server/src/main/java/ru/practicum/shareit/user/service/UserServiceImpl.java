package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String NOT_FOUND_BY_ID_MESSAGE = "Пользователь по указанному идентификатору не найден";

    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper.INSTANCE::toUserDto)
                .toList();
    }

    @Override
    public UserDto findById(Long userId) {
        return UserMapper.INSTANCE.toUserDto(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_BY_ID_MESSAGE)));
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        final User user = UserMapper.INSTANCE.toUser(userDto);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new UserAlreadyExistsException("Пользователь с таким eMail уже существует", exception);
        }
        return UserMapper.INSTANCE.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(Long userId, UserDto userDto) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_BY_ID_MESSAGE));

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        return UserMapper.INSTANCE.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_BY_ID_MESSAGE));

        userRepository.deleteById(userId);

        return UserMapper.INSTANCE.toUserDto(user);
    }
}