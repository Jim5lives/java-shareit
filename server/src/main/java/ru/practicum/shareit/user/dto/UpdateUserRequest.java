package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private String email;

    public boolean hasName() {
        return name != null;
    }

    public boolean hasEmail() {
        return email != null;
    }
}


