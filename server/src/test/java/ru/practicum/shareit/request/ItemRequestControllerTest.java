package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.error.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private ItemRequestDto itemRequestDto;
    private ItemRequestWithItemsDto withResponseDto;
    private final String header = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1);
        itemRequestDto.setDescription("description");
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setRequestor(null);

        withResponseDto = new ItemRequestWithItemsDto();
        withResponseDto.setId(1);
        withResponseDto.setDescription("description");
        withResponseDto.setCreated(LocalDateTime.now());
        withResponseDto.setRequestor(null);
        withResponseDto.setItems(List.of(itemDto));
    }

    @Test
    void createRequest() throws Exception {
        when(itemRequestService.createRequest(anyInt(), any())).thenReturn(itemRequestDto);

        mvc.perform(post("/requests").content(mapper.writeValueAsString(itemRequestDto))
                        .header(header, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is("description")));
    }

    @Test
    void getRequest() throws Exception {
        when(itemRequestService.getRequestById(anyInt(), anyInt()))
                .thenReturn(withResponseDto);

        mvc.perform(get("/requests/{id}",1).header(header,1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(withResponseDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is("description")))
                .andExpect(jsonPath("$.items[0].id", is(1)))
                .andExpect(jsonPath("$.items[0].name", is("name")));
    }

    @Test
    void getRequests() throws Exception {
        when(itemRequestService.getAllUsersRequests(anyInt())).thenReturn(List.of(withResponseDto));

        mvc.perform(get("/requests").header(header, 1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(withResponseDto.getId()), Integer.class));
    }

    @Test
    void getAllRequests() throws Exception {
        when(itemRequestService.getAllRequests(anyInt())).thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests/all").header(header, 1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Integer.class));
    }

    @Test
    void getRequestNotFoundException() throws Exception {
        when(itemRequestService.getRequestById(anyInt(), anyInt()))
                .thenThrow(new NotFoundException("Request not found"));

        mvc.perform(get("/requests/{id}", 1).header(header, 1))

                .andExpect(status().isNotFound());
    }
}