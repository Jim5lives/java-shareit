package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public UserDto createUser(NewUserRequest request) {
        isEmailUnique(request.getEmail());
        User user = UserMapper.mapToUser(request);
        User userCreated = userRepository.save(user);
        log.info("Пользователь успешно создан {}", user);
        return UserMapper.mapToUserDto(userCreated);
    }

    @Override
    @Transactional
    public UserDto updateUser(Integer id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id =" + id));
        if (request.hasEmail()) {
            isEmailUnique(request.getEmail());
        }
        User userToUpdate = updateUserFields(user, request);

        User updatedUser = userRepository.save(userToUpdate);
        log.info("Пользователь успешно обновлен {}", updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id =" + id));
        log.info("Пользователь с id={} успешно найден: {}", id, user);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id =" + id));
        userRepository.deleteById(userToDelete.getId());
        log.info("Пользователь с id={} успешно удален: {}", id, userToDelete);
    }

    private void isEmailUnique(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
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
