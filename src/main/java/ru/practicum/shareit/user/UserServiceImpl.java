package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exceptions.DuplicatedDataException;
import ru.practicum.shareit.user.dto.NewUserRequest;
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
        userRepository.createUser(user);
        return UserMapper.mapToUserDto(user);
    }

    private void isEmailUnique(String email) {
        if (!userRepository.isEmailUnique(email)) {
            throw new DuplicatedDataException("Пользователь с таким email уже зарегистрирован");
        }
    }
}
