package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

public interface BookingService {

    BookingDto createBooking(Integer bookerId, NewBookingRequest request);

    BookingDto approveBooking(Integer ownerId, Integer bookingId, Boolean approved);
}
