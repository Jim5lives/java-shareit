package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.exceptions.BadRequestException;
import ru.practicum.shareit.error.exceptions.AccessForbiddenException;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @MockBean
    BookingService bookingService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private BookingDto bookingDto;
    private final String header = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setEmail("test@gmail.com");
        userDto.setName("name");

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwnerId(1);

        bookingDto = BookingDto.builder().build();
        bookingDto.setId(1);
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));
        bookingDto.setBooker(userDto);
        bookingDto.setItem(itemDto);
        bookingDto.setStatus(BookingStatus.WAITING);
    }

    @Test
    void createBooking() throws Exception {
        NewBookingRequest request = new NewBookingRequest();
        request.setItemId(1);
        request.setStart(LocalDateTime.now().plusHours(1));
        request.setEnd(LocalDateTime.now().plusDays(1));

        when(bookingService.createBooking(anyInt(), any())).thenReturn(bookingDto);
        mvc.perform(post("/bookings").content(mapper.writeValueAsString(request))
                        .header(header, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Integer.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Integer.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void approveBooking() throws Exception {
        when(bookingService.approveBooking(anyInt(), anyInt(), anyBoolean())).thenReturn(bookingDto);
        mvc.perform(patch("/bookings/{id}", 1).param("approved", "true").header(header, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Integer.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Integer.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.findBookingById(anyInt(), anyInt())).thenReturn(bookingDto);
        mvc.perform(get("/bookings/{id}", 1).header(header, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Integer.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Integer.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getOwnerBooking() throws Exception {
        when(bookingService.getAllOwnerBookings(anyInt(), any())).thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings/owner", 1).header(header, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Integer.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Integer.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getBookings() throws Exception {
        when(bookingService.getAllUsersBookings(anyInt(), any())).thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings", 1).header(header, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Integer.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Integer.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getBookingNotFoundException() throws Exception {
        when(bookingService.findBookingById(anyInt(), anyInt()))
                .thenThrow(new NotFoundException("Booking not found"));
        mvc.perform(get("/bookings/{id}", 1).header(header, 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBookingAccessForbiddenException() throws Exception {
        when(bookingService.findBookingById(anyInt(), anyInt()))
                .thenThrow(new AccessForbiddenException("Not authorized"));
        mvc.perform(get("/bookings/{id}", 1).header(header, 1))
                .andExpect(status().isForbidden());
    }

    @Test
    void createBookingValidateException() throws Exception {
        when(bookingService.createBooking(anyInt(), any()))
                .thenThrow(new BadRequestException("Wrong dates"));
        mvc.perform(post("/bookings").content(mapper.writeValueAsString(bookingDto)).header(header, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}