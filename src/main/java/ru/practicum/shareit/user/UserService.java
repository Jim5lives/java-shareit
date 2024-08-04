package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto createUser(NewUserRequest request);
    UserDto updateUser(Integer id, UpdateUserRequest request);
    UserDto findUserById(Integer id);
}
