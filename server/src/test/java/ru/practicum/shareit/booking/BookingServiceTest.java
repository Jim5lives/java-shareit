package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.exceptions.BookingNotValidException;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.item.CommentMapperImpl;
import ru.practicum.shareit.item.ItemMapperImpl;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
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

@SpringBootTest(classes = {BookingServiceImpl.class, BookingMapperImpl.class,
        CommentMapperImpl.class, ItemMapperImpl.class, UserMapperImpl.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final BookingService bookingService;
    @MockBean
    BookingRepository bookingRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    ItemRepository itemRepository;

    @Test
    void createBooking_shouldCreateBooking() {
        Item item = makeItem("item", "desc", true);
        User user = User.builder().id(1).email("createtest@gmail.com").name("created").build();
        NewBookingRequest request = makeNewBookingRequest(1);

        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.save(any()))
                .thenReturn(makeBooking(item, user));

        BookingDto actual = bookingService.createBooking(1, request);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(user.getName(), actual.getBooker().getName());
        assertEquals(item.getName(), actual.getItem().getName());
    }

    @Test
    void createBooking_shouldThrowBookingNotValidExceptionWhenItemIsNotAvailable() {
        Item item = makeItem("item", "desc", false);
        User user = User.builder().id(1).email("createtest@gmail.com").name("created").build();
        NewBookingRequest request = makeNewBookingRequest(1);

        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));

        assertThrows(BookingNotValidException.class, () -> bookingService.createBooking(1, request));
    }

    @Test
    void approveBooking_shouldApproveBookingWhenApprovedIsTrue() {
        Item item = makeItem("item", "desc", true);
        User user = User.builder().id(1).email("createtest@gmail.com").name("created").build();

        when(bookingRepository.save(any()))
                .thenReturn(makeBooking(item, user));
        when(bookingRepository.findByIdAndItemOwnerId(anyInt(), anyInt()))
                .thenReturn(Optional.of(makeBooking(item, user)));

        BookingDto actual = bookingService.approveBooking(1,1, true);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(BookingStatus.APPROVED, actual.getStatus());
        assertEquals(user.getName(), actual.getBooker().getName());
        assertEquals(item.getName(), actual.getItem().getName());
    }

    @Test
    void approveBooking_shouldRejectBookingWhenApprovedIsFalse() {
        Item item = makeItem("item", "desc", true);
        User user = User.builder().id(1).email("createtest@gmail.com").name("created").build();

        when(bookingRepository.save(any()))
                .thenReturn(makeBooking(item, user));
        when(bookingRepository.findByIdAndItemOwnerId(anyInt(), anyInt()))
                .thenReturn(Optional.of(makeBooking(item, user)));

        BookingDto actual = bookingService.approveBooking(1,1, false);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(BookingStatus.REJECTED, actual.getStatus());
        assertEquals(user.getName(), actual.getBooker().getName());
        assertEquals(item.getName(), actual.getItem().getName());
    }

    @Test
    void findBookingById_shouldFindBookingById() {
        Item item = makeItem("item", "desc", true);
        User user = User.builder().id(1).email("createtest@gmail.com").name("created").build();

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(makeBooking(item, user)));

        BookingDto actual = bookingService.findBookingById(1,1);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(user.getName(), actual.getBooker().getName());
        assertEquals(item.getName(), actual.getItem().getName());
    }

    @Test
    void findBookingById_shouldThrowNotFoundExceptionWhenBookingNotFound() {
        User user = User.builder().id(1).email("createtest@gmail.com").name("created").build();

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.findBookingById(1,1));
    }

    @Test
    void getAllUsersBookings_shouldReturnAllUsersBookings() {
        User user = User.builder().id(1).email("createtest@gmail.com").name("created").build();
        Item item = makeItem("item", "desc", true);
        String state = "ALL";

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findByBookerIdOrderByStartDesc(anyInt()))
                .thenReturn(List.of(makeBooking(item, user)));

        List<BookingDto> actual = bookingService.getAllUsersBookings(1, state);

        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void getAllOwnerBookings_shouldReturnAllOwnersBookings() {
        User user = User.builder().id(1).email("createtest@gmail.com").name("created").build();
        Item item = makeItem("item", "desc", true);
        String state = "PAST";

        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(anyInt(), any()))
                .thenReturn(List.of(makeBooking(item, user)));

        List<BookingDto> actual = bookingService.getAllOwnerBookings(1, state);

        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    private NewBookingRequest makeNewBookingRequest(Integer itemId) {
        NewBookingRequest request = new NewBookingRequest();
        request.setItemId(itemId);
        request.setStart(LocalDateTime.now());
        request.setEnd(LocalDateTime.now().plusDays(1));
        return request;
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

    private Item makeItem(String name, String description, boolean available) {
        Item item = new Item();
        item.setId(1);
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setOwnerId(1);
        return item;
    }
}
