package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.NewBookingRequest;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Integer bookerId,
                                @Valid @RequestBody NewBookingRequest request) {
        log.info("Получен запрос на создание бронирования {}", request);
        return bookingClient.createBooking(bookerId, request);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                 @PathVariable Integer bookingId,
                                 @RequestParam Boolean approved) {
        log.info("Получен запрос на подтверждение/отклонение бронирования вещи с id={}", bookingId);
        return bookingClient.approveBooking(ownerId, bookingId, approved);
    }


    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwnerBookings(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                      @RequestParam(defaultValue = "ALL") String stateParam) {
        log.info("Получен запрос на вывод бронирований вещей пользователя с id={}", ownerId);
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getAllOwnerBookings(ownerId, state);
    }
}
