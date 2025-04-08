package ru.yandex.practicum.auth.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super(String.format("Пользователь с id = %d не найден", userId));
    }
}
