package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exceptions.DuplicatedDataException;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(NewUserRequest request) {
        User user = UserMapper.mapToUser(request);
        isEmailUnique(user.getEmail());

        User userCreated = userRepository.createUser(user);
        return UserMapper.mapToUserDto(userCreated);
    }

    @Override
    public UserDto updateUser(Integer id, UpdateUserRequest request) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id =" + id));
        if (request.hasEmail()) {
            isEmailUnique(request.getEmail());
        }
        User userToUpdate = UserMapper.updateUserFields(user, request);

        User updatedUser = userRepository.updateUser(id, userToUpdate);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public UserDto findUserById(Integer id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id =" + id));
        return UserMapper.mapToUserDto(user);
    }

    private void isEmailUnique(String email) {
        if (!userRepository.isEmailUnique(email)) {
            throw new DuplicatedDataException("Пользователь с таким email уже зарегистрирован");
        }
    }
}
