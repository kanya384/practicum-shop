package ru.yandex.practicum.shop.exception;

public class AlreadyExistsInCartException extends RuntimeException {

    public AlreadyExistsInCartException(String message) {
        super(message);
    }
}
