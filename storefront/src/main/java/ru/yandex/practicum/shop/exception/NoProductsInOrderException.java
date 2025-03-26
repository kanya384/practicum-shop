package ru.yandex.practicum.shop.exception;

public class NoProductsInOrderException extends RuntimeException {
    public NoProductsInOrderException(String message) {
        super(message);
    }
}
