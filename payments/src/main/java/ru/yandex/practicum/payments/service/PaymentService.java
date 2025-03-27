package ru.yandex.practicum.payments.service;

import reactor.core.publisher.Mono;
import ru.yandex.practicum.payments.dto.BalanceResponse;
import ru.yandex.practicum.payments.dto.DepositMoneyRequest;
import ru.yandex.practicum.payments.dto.ProcessPaymentRequest;

public interface PaymentService {
    Mono<BalanceResponse> getBalance();

    Mono<BalanceResponse> processPayment(ProcessPaymentRequest request);

    Mono<BalanceResponse> depositMoney(DepositMoneyRequest request);
}
