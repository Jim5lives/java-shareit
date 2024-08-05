package ru.practicum.shareit.error;

import java.util.List;

public record ErrorResponseDetailed(String error, List<String> details) {
}
