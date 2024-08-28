package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Integer bookerId,
                                    @Valid @RequestBody NewBookingRequest request) {
        log.info("Получен запрос на создание бронирования {}", request);
        return bookingService.createBooking(bookerId, request);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                     @PathVariable Integer bookingId,
                                     @RequestParam Boolean approved) {
        log.info("Получен запрос на подтверждение/отклонение бронирования вещи с id={}", bookingId);
        return bookingService.approveBooking(ownerId, bookingId, approved);
    }
}
