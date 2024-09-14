package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class NewUserRequest {
    @NotNull @NotBlank
    private String name;
    @NotNull @NotBlank @Email
    private String email;
}