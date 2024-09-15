package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentDtoTest {
    private final JacksonTester<CommentDto> json;

    @Test
    void testSerialize() throws Exception {
        CommentDto comment = new CommentDto();
        comment.setId(1);
        comment.setText("text");
        comment.setAuthorName("AuthorName");
        comment.setCreated(LocalDateTime.now());

        JsonContent<CommentDto> result = json.write(comment);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.created")
                .hasJsonPath("$.authorName")
                .hasJsonPath("$.text");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(item_id -> assertThat(item_id.intValue()).isEqualTo(comment.getId()));
        assertThat(result).extractingJsonPathStringValue("$.authorName")
                .satisfies(item_name -> assertThat(item_name).isEqualTo(comment.getAuthorName()));
        assertThat(result).extractingJsonPathStringValue("$.text")
                .satisfies(item_description -> assertThat(item_description).isEqualTo(comment.getText()));
        assertThat(result).extractingJsonPathStringValue("$.created")
                .satisfies(created -> assertThat(created).isNotNull());
    }
}