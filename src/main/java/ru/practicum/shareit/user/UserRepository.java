package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {

    User createUser(User user);
    boolean isEmailUnique(String email);
}
