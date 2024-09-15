package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.error.exceptions.DuplicatedDataException;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = {UserServiceImpl.class, UserMapperImpl.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {
    private final UserService userService;
    @MockBean
    UserRepository userRepository;

    @Test
    void createUser_shouldCreateNewUser() {
        String email = "createtest@gmail.com";
        String name = "created";
        NewUserRequest request = makeNewUserRequest(email, name);

        when(userRepository.save(any()))
                .thenReturn(User.builder().id(1).email(email).name(name).build());

        UserDto expectedUser = makeUserDto(email, name);

        UserDto actualUser = userService.createUser(request);

        assertNotNull(actualUser);
        assertNotNull(actualUser.getId());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
    }

    @Test
    void updateUser_shouldUpdateUser() {
        UserDto oldUser = makeUserDto( "oldEmail@gmail.com", "oldName");
        String email = "updatedEmail@gmail.com";
        String name = "updatedName";
        UpdateUserRequest request = makeUpdateUserRequest(email, name);

        when(userRepository.save(any()))
                .thenReturn(User.builder().id(1).email(email).name(name).build());
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(User.builder().id(1).email("oldEmail@gmail.com").name("oldName").build()));

        UserDto expectedUser = makeUserDto(email, name);

        UserDto actualUser = userService.updateUser(1, request);

        assertNotNull(actualUser);
        assertNotNull(actualUser.getId());
        assertNotEquals(oldUser.getName(), actualUser.getName());
        assertNotEquals(oldUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
    }

    @Test
    void findUserById_shouldReturnUserById() {
        String email = "createtest@gmail.com";
        String name = "created";
        UserDto expectedUser = makeUserDto(email, name);

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(User.builder().id(1).email("createtest@gmail.com").name("created").build()));

        UserDto actualUser = userService.findUserById(1);

        assertNotNull(actualUser);
        assertNotNull(actualUser.getId());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
    }

    @Test
    void findUserById_shouldThrowNotFoundExceptionWhenUserNotFound() {
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findUserById(1));
    }

    @Test
    void createUser_shouldThrowDuplicatedDataExceptionWhenEmailIsNotUnique() {
        String email = "createtest@gmail.com";
        String name = "created";
        NewUserRequest request = makeNewUserRequest(email, name);

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(User.builder().id(1).email("createtest@gmail.com").name("created").build()));

        assertThrows(DuplicatedDataException.class, () -> userService.createUser(request));
    }


    private UserDto makeUserDto(String email, String name) {
        UserDto dto = new UserDto();
        dto.setId(1);
        dto.setName(name);
        dto.setEmail(email);
        return dto;
    }

    private NewUserRequest makeNewUserRequest(String email, String name) {
        NewUserRequest request = new NewUserRequest();
        request.setName(name);
        request.setEmail(email);
        return request;
    }

    private UpdateUserRequest makeUpdateUserRequest(String email, String name) {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setName(name);
        request.setEmail(email);
        return request;
    }
}