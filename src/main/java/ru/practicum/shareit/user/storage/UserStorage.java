package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> findAll();

    User findOneById(Long userId);

    User create(User user);

    User update(Long userId, User user);

    User delete(Long userId);

    boolean isUserExists(Long userId);

    boolean isEmailExists(String email);
}