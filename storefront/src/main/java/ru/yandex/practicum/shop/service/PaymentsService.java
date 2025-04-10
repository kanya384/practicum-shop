package ru.yandex.practicum.shop.service;

import reactor.core.publisher.Mono;

public interface PaymentsService {
    Mono<Integer> put5kToBalance();

    Mono<Integer> getBalance();

    Mono<Integer> processPayment(int orderSum);
}
