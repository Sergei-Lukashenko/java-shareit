package ru.practicum.shareit.user.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.UserAlreadyExists;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Primary
@Repository
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long maxId = 0L;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findOneById(final Long id) {
        return users.get(id);
    }

    @Override
    public User create(final User user) {
        user.setId(++maxId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(final Long userId, final User user) {
        final User storedUser = findOneById(userId);

        if (user.getName() != null) {
            storedUser.setName(user.getName());
        }

        final String newUserEmail = user.getEmail();
        if (newUserEmail != null && !storedUser.getEmail().equals(newUserEmail)) {
            if (isEmailExists(newUserEmail)) {
                throw new UserAlreadyExists("Такой email пользователя уже занят");
            }
            storedUser.setEmail(newUserEmail);
        }

        user.setId(userId);
        users.put(userId, storedUser);

        return storedUser;
    }

    @Override
    public User delete(final Long userId) {
        return users.remove(userId);
    }

    @Override
    public boolean isUserExists(final Long userId) {
        return users.containsKey(userId);
    }

    @Override
    public boolean isEmailExists(final String email) {
        return users.values().stream().anyMatch(u -> email != null && email.equals(u.getEmail()));
    }
}