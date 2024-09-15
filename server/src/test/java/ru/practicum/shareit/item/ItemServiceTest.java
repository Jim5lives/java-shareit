package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.BookingMapperImpl;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.exceptions.AccessForbiddenException;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
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

@SpringBootTest(classes = {ItemServiceImpl.class, ItemMapperImpl.class, BookingMapperImpl.class, UserMapperImpl.class,
        CommentMapperImpl.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private final ItemService itemService;
    @MockBean
    ItemRepository itemRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    BookingRepository bookingRepository;
    @MockBean
    CommentRepository commentRepository;
    @MockBean
    ItemRequestRepository itemRequestRepository;

    @Test
    void createItem_shouldCreateItem() {
        String name = "testName";
        String description = "testDescription";
        boolean available = true;
        NewItemRequest request = makeNewItemRequest(name, description, available);

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(User.builder().id(1).email("createtest@gmail.com").name("created").build()));
        when(itemRepository.save(any()))
                .thenReturn(new Item(1, name, description,available, 1, null));

        ItemDto expectedItem = makeItemDto(name, description, available);

        ItemDto actualItem = itemService.createItem(1, request);

        assertNotNull(actualItem);
        assertNotNull(actualItem.getId());
        assertNotNull(actualItem.getOwnerId());
        assertEquals(expectedItem.getName(), actualItem.getName());
        assertEquals(expectedItem.getDescription(), actualItem.getDescription());
    }

    @Test
    void updateItem_shouldUpdateItem() {
        ItemDto oldItem = makeItemDto("oldItem", "oldDesc", true);
        String name = "updatedName";
        String description = "updatedDescription";
        boolean available = false;
        UpdateItemRequest request = makeUpdateItemRequest(name, description, available);

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(User.builder().id(1).email("createtest@gmail.com").name("created").build()));
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(new Item(
                        1, "oldItem", "oldDesc", false, 1, null)));
        when(itemRepository.save(any()))
                .thenReturn(new Item(1, name, description,available, 1, null));

        ItemDto expectedItem = makeItemDto(name, description, available);

        ItemDto actualItem = itemService.updateItem(1, 1, request);

        assertNotNull(actualItem);
        assertNotNull(actualItem.getId());
        assertNotNull(actualItem.getOwnerId());
        assertNotEquals(oldItem, actualItem);
        assertEquals(expectedItem.getName(), actualItem.getName());
        assertEquals(expectedItem.getDescription(), actualItem.getDescription());
    }

    @Test
    void findItemById_shouldReturnItemById() {
        String name = "testName";
        String description = "testDescription";
        boolean available = true;

        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(new Item(
                        1, name, description, available, 1, null)));

        ItemDto expectedItem = makeItemDto(name, description, available);

        ItemDto actualItem = itemService.findItemById(1, 1);

        assertNotNull(actualItem);
        assertNotNull(actualItem.getId());
        assertNotNull(actualItem.getOwnerId());
        assertEquals(expectedItem.getName(), actualItem.getName());
        assertEquals(expectedItem.getDescription(), actualItem.getDescription());
    }

    @Test
    void findItemById_shouldThrowNotFoundExceptionWhenItemNotFound() {
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.findItemById(1,1));
    }

    @Test
    void deleteItem_shouldThrowAccessForbiddenExceptionWhenOwnerIdAndUserIdNotMatching() {
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(User.builder().id(1).email("createtest@gmail.com").name("created").build()));
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(new Item(
                        1, "oldItem", "oldDesc", false, 2, null)));


        assertThrows(AccessForbiddenException.class, () -> itemService.deleteItem(1,1));
    }

    @Test
    void getAllUsersItems_shouldReturnAllUsersItems() {
        Item item1 = makeItem("item1", "desc1", true);
        Item item2 = makeItem("item2", "desc2", true);

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(User.builder().id(1).email("createtest@gmail.com").name("created").build()));
        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(anyInt()))
                .thenReturn(List.of());
        when(itemRepository.findAllByOwnerId(anyInt()))
                .thenReturn(List.of(item1, item2));

        List<ItemCommentDto> actualList = itemService.getAllUsersItems(1);

        assertNotNull(actualList);
        assertEquals(2, actualList.size());
    }

    @Test
    void search_shouldReturnItems() {
        String query = "test";
        Item item1 = makeItem("test1", "desc1", true);
        Item item2 = makeItem("test2", "desc2", true);

        when(itemRepository.search(query))
                .thenReturn(List.of(item1, item2));

        List<ItemDto> actual = itemService.search(query);

        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    void search_shouldReturnEmptyListWhenQueryIsBlank() {
        String query = "";

        List<ItemDto> actual = itemService.search(query);

        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    void addComment_shouldAddComment() {
        User user = User.builder().id(1).email("test@gmail.com").name("user").build();
        Item item = makeItem("item", "desc", false);
        String text = "thisIsCleverComment";
        NewCommentRequest request = new NewCommentRequest();
        request.setText(text);

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(User.builder().id(1).email("test@gmail.com").name("user").build()));
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(
                        new Item(1, "item", "desc", true, 1, null)));
        when(bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndBefore(any(), any(), any(), any()))
                .thenReturn(List.of(makeBooking(item, user)));
        when(commentRepository.save(any()))
                .thenReturn(makeComment(text, item, user));

        CommentDto actual = itemService.addComment(1,1, request);

        assertNotNull(actual);
        assertEquals(actual.getText(), text);
        assertEquals(actual.getAuthorName(), user.getName());
        assertEquals(actual.getItemId(), item.getId());
    }

    private NewItemRequest makeNewItemRequest(String name, String description, Boolean available) {
        NewItemRequest request = new NewItemRequest();
        request.setName(name);
        request.setDescription(description);
        request.setAvailable(available);
        return request;
    }

    private UpdateItemRequest makeUpdateItemRequest(String name, String description, Boolean available) {
        UpdateItemRequest request = new UpdateItemRequest();
        request.setName(name);
        request.setDescription(description);
        request.setAvailable(available);
        return request;
    }

    private ItemDto makeItemDto(String name, String description, boolean available) {
        ItemDto dto = new ItemDto();
        dto.setId(1);
        dto.setName(name);
        dto.setDescription(description);
        dto.setAvailable(available);
        dto.setOwnerId(1);
        return dto;
    }

    private Item makeItem(String name, String description, boolean available) {
        Item item = new Item();
        item.setId(1);
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setOwnerId(1);
        return item;
    }

    private Booking makeBooking(Item item, User user) {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);
        return booking;
    }

    private Comment makeComment(String text, Item item, User user) {
        Comment comment = new Comment();
        comment.setId(1);
        comment.setText(text);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

}
