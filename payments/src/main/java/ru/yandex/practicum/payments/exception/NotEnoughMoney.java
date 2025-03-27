package ru.yandex.practicum.payments.exception;

public class NotEnoughMoney extends Exception {
    public NotEnoughMoney(String message) {
        super(message);
    }
}
