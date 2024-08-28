package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(Integer bookerId, NewBookingRequest request);

    BookingDto approveBooking(Integer ownerId, Integer bookingId, Boolean approved);

    BookingDto findBookingById(Integer userId, Integer bookingId);

    List<BookingDto> getAllUsersBookings(Integer bookerId, String state);

    List<BookingDto> getAllOwnerBookings(Integer ownerId, String state);
}
