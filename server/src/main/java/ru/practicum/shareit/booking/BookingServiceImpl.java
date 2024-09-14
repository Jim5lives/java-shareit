package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.error.exceptions.AccessForbiddenException;
import ru.practicum.shareit.error.exceptions.BadRequestException;
import ru.practicum.shareit.error.exceptions.BookingNotValidException;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;


    @Override
    @Transactional
    public BookingDto createBooking(Integer bookerId, NewBookingRequest request) {
        User booker = findUserById(bookerId);
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + request.getItemId()));

        if (Boolean.FALSE.equals(item.getAvailable())) {
            throw new BookingNotValidException("Вещь недоступна для бронирования");
        }
        if (!request.getStart().isBefore(request.getEnd())) {
            throw new BookingNotValidException("Начало времени бронирования должно быть до времени конца бронирования");
        }

        Booking booking = bookingMapper.mapToBooking(request, booker, item);
        booking = bookingRepository.save(booking);
        log.info("Создан запрос на бронирование вещи {} от пользователя с id={}", item, bookerId);
        return bookingMapper.mapToBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Integer ownerId, Integer bookingId, Boolean approved) {
        Booking booking = bookingRepository.findByIdAndItemOwnerId(bookingId, ownerId)
                .orElseThrow(() -> new AccessForbiddenException(
                        "Не найдено бронирование с id = " + bookingId + "у пользователя с id = " + ownerId));

        if (Boolean.TRUE.equals(approved)) {
            booking.setStatus(BookingStatus.APPROVED);
            log.info("Бронирование с id={} подтверждено владельцем вещи", bookingId);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            log.info("Бронирование с id={} отклонено владельцем вещи", bookingId);
        }

        bookingRepository.save(booking);
        return bookingMapper.mapToBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto findBookingById(Integer userId, Integer bookingId) {
        User user = findUserById(userId);
        Booking booking = findBookingById(bookingId);
        if (user.getId().equals(booking.getBooker().getId()) || user.getId().equals(booking.getItem().getOwnerId())) {
            log.info("Бронирование с id={} найдено", bookingId);
            return bookingMapper.mapToBookingDto(booking);
        } else {
            throw new AccessForbiddenException("Нет прав для просмотра бронирования");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllUsersBookings(Integer bookerId, String state) {
        User booker = findUserById(bookerId);
        BookingState bookingState = validateBookingState(state);

        return switch (bookingState) {
            case ALL -> bookingMapper.mapToListBookingDto(
                    bookingRepository.findByBookerIdOrderByStartDesc(bookerId));
            case PAST -> bookingMapper.mapToListBookingDto(
                    bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(booker.getId(), LocalDateTime.now()));
            case CURRENT -> bookingMapper.mapToListBookingDto(
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.APPROVED));
            case FUTURE -> bookingMapper.mapToListBookingDto(
                    bookingRepository.findByBookerIdAndStartAfter(bookerId, LocalDateTime.now()));
            case WAITING -> bookingMapper.mapToListBookingDto(
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING));
            case REJECTED -> bookingMapper.mapToListBookingDto(
                    bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED));
        };
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllOwnerBookings(Integer ownerId, String state) {
        User owner = findUserById(ownerId);
        BookingState bookingState = validateBookingState(state);

        return switch (bookingState) {
            case ALL -> bookingMapper.mapToListBookingDto(
                    bookingRepository.findByItemOwnerIdOrderByStartDesc(owner.getId()));
            case PAST -> bookingMapper.mapToListBookingDto(
                    bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now()));
            case CURRENT -> bookingMapper.mapToListBookingDto(
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.APPROVED));
            case FUTURE -> bookingMapper.mapToListBookingDto(
                    bookingRepository.findByItemOwnerIdAndStartAfter(ownerId, LocalDateTime.now()));
            case WAITING -> bookingMapper.mapToListBookingDto(
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING));
            case REJECTED -> bookingMapper.mapToListBookingDto(
                    bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED));
        };

    }

    private Booking findBookingById(Integer bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не найдено бронирование с id =" + bookingId));
    }

    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id =" + userId));
    }

    private BookingState validateBookingState(String state) {
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state.toUpperCase());
            log.debug("Статус бронирования корректен: {}", bookingState);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Неподдерживаемый формат state: " + state);
        }
        return bookingState;
    }
}
