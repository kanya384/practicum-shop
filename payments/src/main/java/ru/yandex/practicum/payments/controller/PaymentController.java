package ru.yandex.practicum.payments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.payments.api.PaymentsApi;
import ru.yandex.practicum.payments.dto.BalanceResponse;
import ru.yandex.practicum.payments.dto.DepositMoneyRequest;
import ru.yandex.practicum.payments.dto.ProcessPaymentRequest;
import ru.yandex.practicum.payments.service.PaymentService;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentsApi {
    private final PaymentService paymentService;

    @Override
    public Mono<BalanceResponse> paymentsCreateUserIdPost(Long userId, ServerWebExchange exchange) {
        return paymentService.createAccountAndPutMoneyToBalance(userId);
    }

    @Override
    public Mono<BalanceResponse> paymentsBalanceUserIdGet(Long userId, ServerWebExchange exchange) {
        return paymentService.getBalance(userId);
    }

    @Override
    public Mono<BalanceResponse> paymentsDepositUserIdPost(Long userId, Mono<DepositMoneyRequest> depositMoneyRequest, ServerWebExchange exchange) {
        return depositMoneyRequest
                .flatMap(request -> paymentService.depositMoney(userId, request));
    }

    @Override
    public Mono<BalanceResponse> paymentsProcessUserIdPost(Long userId, Mono<ProcessPaymentRequest> processPaymentRequest, ServerWebExchange exchange) {
        return processPaymentRequest
                .flatMap(request -> paymentService.processPayment(userId, request));
    }
}
