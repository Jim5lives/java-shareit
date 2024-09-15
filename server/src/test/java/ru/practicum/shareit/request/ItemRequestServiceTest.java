package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemMapperImpl;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserMapperImpl;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ItemRequestServiceImpl.class, ItemRequestMapperImpl.class,
        UserMapperImpl.class, ItemMapperImpl.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    private final ItemRequestService itemRequestService;
    @MockBean
    ItemRequestRepository itemRequestRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    ItemRepository itemRepository;

    @Test
    void createRequest_shouldCreateRequest() {
        User user = User.builder().id(1).email("createtest@gmail.com").name("created").build();
        String description = "testDescription";
        NewItemRequestRequest request = makeNewItemRequestRequest(description);

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any()))
                .thenReturn(makeItemRequest(description,user));

        ItemRequestDto actual = itemRequestService.createRequest(1, request);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(actual.getDescription(), description);
        assertEquals(actual.getRequestor().getName(), user.getName());
    }

    @Test
    void getAllRequests_shouldReturnAllRequests() {
        User user = User.builder().id(1).email("createtest@gmail.com").name("created").build();
        ItemRequest itemRequest1 = makeItemRequest("desc1", user);
        ItemRequest itemRequest2 = makeItemRequest("desc2", user);

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequestorIdNot(anyInt()))
                .thenReturn(List.of(itemRequest1, itemRequest2));

        List<ItemRequestDto> actual = itemRequestService.getAllRequests(1);

        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    void getRequestById_shouldReturnRequestById() {
        User user = User.builder().id(1).email("createtest@gmail.com").name("created").build();
        ItemRequest itemRequest = makeItemRequest("desc1", user);

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyInt()))
                .thenReturn(Optional.of(itemRequest));

        ItemRequestWithResponseDto actual = itemRequestService.getRequestById(1, 1);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(user, actual.getRequestor());
    }

    @Test
    void getRequestById_shouldThrowNotFoundExceptionWhenRequestNotFound() {
        User user = User.builder().id(1).email("createtest@gmail.com").name("created").build();

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.getRequestById(1,1));
    }

    @Test
    void getAllUsersRequests_shouldReturnAllUsersRequests() {
        User user = User.builder().id(1).email("createtest@gmail.com").name("created").build();
        ItemRequest itemRequest1 = makeItemRequest("desc1", user);
        ItemRequest itemRequest2 = makeItemRequest("desc2", user);

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequestorId(anyInt()))
                .thenReturn(List.of(itemRequest1, itemRequest2));
        when(itemRequestRepository.findById(1))
                .thenReturn(Optional.of(itemRequest1));
        when(itemRequestRepository.findById(2))
                .thenReturn(Optional.of(itemRequest2));

        List<ItemRequestWithResponseDto> actual = itemRequestService.getAllUsersRequests(1);

        assertNotNull(actual);
        assertEquals(2, actual.size());
    }


    private NewItemRequestRequest makeNewItemRequestRequest(String description) {
        NewItemRequestRequest request = new NewItemRequestRequest();
        request.setDescription(description);
        return request;
    }

    private ItemRequest makeItemRequest(String description, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription(description);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }
}
