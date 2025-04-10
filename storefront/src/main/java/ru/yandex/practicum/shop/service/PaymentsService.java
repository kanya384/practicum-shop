package ru.yandex.practicum.shop.service;

import reactor.core.publisher.Mono;

public interface PaymentsService {
    Mono<Integer> put5kToBalance(Long userId);

    Mono<Integer> getBalance(Long userId);

    Mono<Integer> processPayment(Long userId, int orderSum);
}
