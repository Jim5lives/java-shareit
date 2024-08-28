package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Data
@Validated
public class NewBookingRequest {
    @NotNull @Positive
    private Integer itemId;
    @Future @NotNull
    private LocalDateTime start;
    @Future @NotNull
    private LocalDateTime end;
}
