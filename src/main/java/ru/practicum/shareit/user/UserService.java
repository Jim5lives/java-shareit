package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto createUser(NewUserRequest request);
}
