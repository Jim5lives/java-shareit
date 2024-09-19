package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws Exception {
        UserDto user = new UserDto();
        user.setId(1);
        user.setName("name");
        user.setEmail("email@gmail.com");

        ItemDto item = new ItemDto();
        item.setId(1);
        item.setName("Name");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwnerId(1);
        item.setRequestId(1);

        BookingDto booking = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .booker(user)
                .item(item)
                .status(BookingStatus.APPROVED)
                .build();

        JsonContent<BookingDto> result = json.write(booking);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.start")
                .hasJsonPath("$.end")
                .hasJsonPath("$.status")
                .hasJsonPath("$.booker.id")
                .hasJsonPath("$.booker.name")
                .hasJsonPath("$.booker.email")
                .hasJsonPath("$.item.id")
                .hasJsonPath("$.item.available")
                .hasJsonPath("$.item.description")
                .hasJsonPath("$.item.name");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(item_id -> assertThat(item_id.intValue()).isEqualTo(booking.getId()));
        assertThat(result).extractingJsonPathStringValue("$.start")
                .satisfies(created -> assertThat(created).isNotNull());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .satisfies(created -> assertThat(created).isNotNull());
        assertThat(result).extractingJsonPathValue("$.status")
                .satisfies(status -> assertThat(status).isEqualTo(booking.getStatus().name()));

        assertThat(result).extractingJsonPathNumberValue("$.booker.id")
                .satisfies(item_id -> assertThat(item_id.intValue()).isEqualTo(booking.getBooker().getId()));
        assertThat(result).extractingJsonPathStringValue("$.booker.name")
                .satisfies(item_description -> assertThat(item_description).isEqualTo(booking.getBooker().getName()));
        assertThat(result).extractingJsonPathStringValue("$.booker.email")
                .satisfies(item_description -> assertThat(item_description).isEqualTo(booking.getBooker().getEmail()));

        assertThat(result).extractingJsonPathNumberValue("$.item.id")
                .satisfies(id -> assertThat(id.intValue()).isEqualTo(booking.getItem().getId()));

        assertThat(result).extractingJsonPathStringValue("$.item.name")
                .satisfies(item_name -> assertThat(item_name).isEqualTo(booking.getItem().getName()));
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .satisfies(item_description -> assertThat(item_description).isEqualTo(booking.getItem().getDescription()));
        assertThat(result).extractingJsonPathBooleanValue("$.item.available")
                .satisfies(item_available -> assertThat(item_available).isEqualTo(booking.getItem().getAvailable()));
    }
}