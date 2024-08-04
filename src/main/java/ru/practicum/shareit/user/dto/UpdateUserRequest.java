package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
public class UpdateUserRequest {
    @NotNull
    private Integer id;
    @NotNull @NotBlank
    private String name;
    @NotNull @NotBlank @Email
    private String email;

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasEmail() {
        return email != null && !email.isBlank();
    }
}


