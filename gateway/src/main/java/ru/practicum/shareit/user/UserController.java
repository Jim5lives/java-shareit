package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public Object createUser(@Valid @RequestBody NewUserRequest request) {
        log.info("Получен запрос на создание пользователя {}", request);
        return userClient.createUser(request);
    }

    @PatchMapping("/{id}")
    public Object updateUser(@PathVariable Integer id,
                             @Valid @RequestBody UpdateUserRequest request) {
        log.info("Получен запрос на обновление пользователя с id={}", id);
        return userClient.updateUser(id, request);
    }

    @GetMapping("/{id}")
    public Object findUserById(@PathVariable @Positive Integer id) {
        log.info("Получен запрос на поиск пользователя с id={}", id);
        return userClient.findUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable @Positive Integer id) {
        log.info("Получен запрос на удаление пользователя с id={}", id);
        userClient.deleteUser(id);
    }
}