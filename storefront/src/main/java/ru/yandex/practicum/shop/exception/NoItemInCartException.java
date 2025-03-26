package ru.yandex.practicum.shop.exception;

public class NoItemInCartException extends RuntimeException {
    public NoItemInCartException(String message) {
        super(message);
    }
}
