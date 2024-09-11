package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface BookingMapper {

    @Mapping(target = "status", constant = "WAITING")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booker", source = "user")
    Booking mapToBooking(NewBookingRequest request, User user, Item item);

    @Mapping(source = "item", target = "item")
    @Mapping(source = "booker", target = "booker")
    @Mapping(source = "status", target = "status")
    BookingDto mapToBookingDto(Booking booking);

    List<BookingDto> mapToListBookingDto(List<Booking> bookingList);

    @Named("mapItemToItemDto")
    default ItemDto mapItemToItemDto(Item item, @Context ItemMapper itemMapper) {
        return itemMapper.mapToItemDto(item);
    }

    @Named("mapUserToUserDto")
    default UserDto mapUserToUserDto(User user, @Context UserMapper userMapper) {
        return userMapper.mapToUserDto(user);
    }
}

