package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @MockBean
    ItemService itemService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private ItemCommentDto itemCommentDto;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private final String header = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwnerId(1);

        itemCommentDto = new ItemCommentDto();
        itemCommentDto.setId(1);
        itemCommentDto.setName("name");
        itemCommentDto.setDescription("description");
        itemCommentDto.setAvailable(true);
        itemCommentDto.setOwnerId(1);

        commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("comment");
        commentDto.setAuthorName("author");
        commentDto.setItemId(1);
        commentDto.setCreated(LocalDateTime.now());
    }

    @Test
    void createItem() throws Exception {
        when(itemService.createItem(anyInt(), any())).thenReturn(itemDto);
        mvc.perform(post("/items").header(header, 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemDto.getOwnerId()), Integer.class));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(anyInt(), anyInt(), any())).thenReturn(itemDto);
        mvc.perform(patch("/items/{id}", 1).header(header, 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemDto.getOwnerId()), Integer.class));

    }

    @Test
    void findItemById() throws Exception {
        when(itemService.findItemById(anyInt(), anyInt())).thenReturn(itemCommentDto);
        mvc.perform(get("/items/{id}", 1).header(header, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemCommentDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemCommentDto.getName())))
                .andExpect(jsonPath("$.description", is(itemCommentDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemCommentDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemCommentDto.getOwnerId()), Integer.class));
    }

    @Test
    void getItems() throws Exception {
        when(itemService.getAllUsersItems(anyInt())).thenReturn(List.of(itemCommentDto));
        mvc.perform(get("/items", 1).header(header, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(itemCommentDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].name", is(itemCommentDto.getName())));
    }

    @Test
    void search() throws Exception {
        when(itemService.search(anyString())).thenReturn(List.of(itemDto));
        mvc.perform(get("/items/search").header(header, 1).param("text", anyString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())));
    }

    @Test
    void addComment() throws Exception {
        when(itemService.addComment(anyInt(), anyInt(), any())).thenReturn(commentDto);
        mvc.perform(post("/items/{id}/comment", 1).header(header, 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Integer.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.itemId", is(commentDto.getItemId()), Integer.class))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }

    @Test
    void getItemNotFoundException() throws Exception {
        when(itemService.findItemById(anyInt(), anyInt()))
                .thenThrow(new NotFoundException("Item not found"));
        mvc.perform(get("/items/{id}", 999).header(header, 1))
                .andExpect(status().isNotFound());
    }
}