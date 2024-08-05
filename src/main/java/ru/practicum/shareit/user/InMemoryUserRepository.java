package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Map;
import java.util.Optional;

@Slf4j
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
        log.info("Пользователь сохранен в память");
        return user;
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public User updateUser(Integer id, User user) {
        return userMap.put(id, user);
    }

    @Override
    public void deleteUser(Integer id) {
        userMap.remove(id);
        log.info("Пользователь удален из памяти");
    }

    @Override
    public boolean isEmailUnique(String email) {
        log.info("Проводится поиск дубликатов email: {}", email);
        return userMap.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst().isEmpty();
    }

    private Integer generateId() {
        log.info("Сгенерирован новый userId={}", id);
        return id++;
    }
}
