package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exceptions.DuplicatedDataException;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(NewUserRequest request) {
        User user = UserMapper.mapToUser(request);
        isEmailUnique(user.getEmail());

        User userCreated = userRepository.createUser(user);
        log.info("Пользователь успешно создан {}", user);
        return UserMapper.mapToUserDto(userCreated);
    }

    @Override
    public UserDto updateUser(Integer id, UpdateUserRequest request) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id =" + id));
        if (request.hasEmail()) {
            isEmailUnique(request.getEmail());
        }
        User userToUpdate = updateUserFields(user, request);

        User updatedUser = userRepository.updateUser(id, userToUpdate);
        log.info("Пользователь успешно обновлен {}", user);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public UserDto findUserById(Integer id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id =" + id));

        log.info("Пользователь с id={} успешно найден: {}", id, user);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public void deleteUser(Integer id) {
        User userToDelete = userRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id =" + id));

        userRepository.deleteUser(userToDelete.getId());
        log.info("Пользователь с id={} успешно удален: {}", id, userToDelete);
    }

    private void isEmailUnique(String email) {
        if (!userRepository.isEmailUnique(email)) {
            log.warn("Ошибка: email {} уже используется", email);
            throw new DuplicatedDataException("Пользователь с таким email уже зарегистрирован");
        }
    }

    private User updateUserFields(User user, UpdateUserRequest request) {
        if (request.hasName()) {
            if (request.getName().isBlank()) {
                log.warn("Передано пустое имя пользователя для обновления");
                throw new ValidationException("Имя пользователя не может быть пустым");
            }
            user.setName(request.getName());
        }

        if (request.hasEmail()) {
            if (request.getEmail().isBlank()) {
                log.warn("Передан пустой email пользователя для обновления");
                throw new ValidationException("Email не может быть пустым");
            }
            user.setEmail(request.getEmail());
        }
        return user;
    }
}
