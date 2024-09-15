package ru.practicum.shareit.request;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    @SneakyThrows
    public void testItemRequestDtoSerialize() {
        UserDto user = new UserDto();
        user.setId(1);
        user.setName("name");
        user.setEmail("email@gmail.com");

        ItemRequestDto request = new ItemRequestDto();
        request.setId(1);
        request.setDescription("Description");
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());

        JsonContent<ItemRequestDto> content = json.write(request);

        assertThat(content)
                .hasJsonPath("$.id")
                .hasJsonPath("$.description")
                .hasJsonPath("$.requestor")
                .hasJsonPath("$.created");
        assertThat(content).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("@.description").isEqualTo("Description");
        assertThat(content).extractingJsonPathStringValue("@.requestor.name").isEqualTo("name");
    }
}