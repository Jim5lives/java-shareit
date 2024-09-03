package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByBookerIdOrderByStartDesc(Integer bookerId);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Integer bookerId, LocalDateTime time);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Integer bookerId, BookingStatus status);

    List<Booking> findByBookerIdAndStartAfter(Integer bookerId, LocalDateTime time);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Integer ownerId);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(Integer ownerId, LocalDateTime time);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Integer ownerId, BookingStatus status);

    List<Booking> findByItemOwnerIdAndStartAfter(Integer ownerId, LocalDateTime time);

    List<Booking> findAllByBookerIdAndItemIdAndStatusAndEndBefore(Integer userId, Integer itemId,
                                                                  BookingStatus status, LocalDateTime date);

    List<Booking> findByItemId(Integer itemId);

}
