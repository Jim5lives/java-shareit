package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {

    User createUser(User user);

    Optional<User> findUserById(Integer id);

    User updateUser(Integer id, User user);

    void deleteUser(Integer id);

    boolean isEmailUnique(String email);

}
