package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody NewUserRequest request) {
        log.info("Получен запрос на создание пользователя {}", request);
        return userService.createUser(request);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Integer id,
                              @RequestBody UpdateUserRequest request) {
        log.info("Получен запрос на обновление пользователя с id={}", id);
        return userService.updateUser(id, request);
    }

    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable Integer id) {
        log.info("Получен запрос на поиск пользователя с id={}", id);
        return userService.findUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        log.info("Получен запрос на удаление пользователя с id={}", id);
        userService.deleteUser(id);
    }
}