package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.exceptions.AccessForbiddenException;
import ru.practicum.shareit.error.exceptions.BookingNotValidException;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDto createBooking(Integer bookerId, NewBookingRequest request) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id =" + bookerId));
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + request.getItemId()));

        if (Boolean.FALSE.equals(item.getAvailable())) {
            throw new BookingNotValidException("Вещь недоступна для бронирования");
        }
        if (!request.getStart().isBefore(request.getEnd())) {
            throw new BookingNotValidException("Начало времени бронирования должно быть до времени конца бронирования");
        }

        Booking booking = BookingMapper.mapToBooking(request, booker, item);
        booking = bookingRepository.save(booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Integer ownerId, Integer bookingId, Boolean approved) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new AccessForbiddenException("Не найден пользователь с id =" + ownerId));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не найдено бронирование с id =" + bookingId));

        if (!owner.getId().equals(booking.getItem().getOwnerId())) {
            throw new AccessForbiddenException("Нет прав для обработки бронирования у пользователя с id=" + ownerId);
        }
        if (Boolean.TRUE.equals(approved)) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        bookingRepository.save(booking);
        return BookingMapper.mapToBookingDto(booking);
    }
}
