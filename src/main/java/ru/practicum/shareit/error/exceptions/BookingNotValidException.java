package ru.practicum.shareit.error.exceptions;

public class BookingNotValidException extends RuntimeException{
    public BookingNotValidException(String message) {
        super(message);
    }
}
