package ru.yandex.practicum.payments.service;

import reactor.core.publisher.Mono;
import ru.yandex.practicum.payments.dto.BalanceResponse;
import ru.yandex.practicum.payments.dto.DepositMoneyRequest;
import ru.yandex.practicum.payments.dto.ProcessPaymentRequest;

public interface PaymentService {
    Mono<BalanceResponse> createAccountAndPutMoneyToBalance(Long userId);

    Mono<BalanceResponse> getBalance(Long userId);

    Mono<BalanceResponse> processPayment(Long userId, ProcessPaymentRequest request);

    Mono<BalanceResponse> depositMoney(Long userId, DepositMoneyRequest request);
}
