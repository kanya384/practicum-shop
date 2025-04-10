package ru.yandex.practicum.payments.service;

import reactor.core.publisher.Mono;
import ru.yandex.practicum.payments.dto.BalanceResponse;
import ru.yandex.practicum.payments.dto.DepositMoneyRequest;
import ru.yandex.practicum.payments.dto.ProcessPaymentRequest;

public interface PaymentService {
    Mono<BalanceResponse> createAccountAndPutMoneyToBalance(long userId);

    Mono<BalanceResponse> getBalance(long userId);

    Mono<BalanceResponse> processPayment(long userId, ProcessPaymentRequest request);

    Mono<BalanceResponse> depositMoney(long userId, DepositMoneyRequest request);
}
