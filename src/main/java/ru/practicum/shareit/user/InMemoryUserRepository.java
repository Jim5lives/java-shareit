package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {
    private final Map<Integer, User> userMap;
    private Integer id = 1;

    @Override
    public User createUser(User user) {
        Integer userId = generateId();
        user.setId(userId);
        userMap.put(userId, user);
        return user;
    }

    @Override
    public boolean isEmailUnique(String email) {
        return userMap.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst().isEmpty();
    }

    private Integer generateId() {
        return id++;
    }
}
