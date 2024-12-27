package ru.practicum.shareit.user.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        users.put(userId, user);
        return user;
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
    public long userIdHavingEmail(final String email) {
        Optional<User> userWithEmail = users.values().stream()
                .filter(u -> email != null && email.equals(u.getEmail()))
                .findFirst();
        if (userWithEmail.isPresent()) {
            return userWithEmail.get().getId();
        } else {
            return 0L;
        }
    }
}