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
    public Mono<BalanceResponse> paymentsBalanceGet(ServerWebExchange exchange) {
        return paymentService.getBalance();
    }

    @Override
    public Mono<BalanceResponse> paymentsDepositPost(Mono<DepositMoneyRequest> depositMoneyRequest, ServerWebExchange exchange) {
        return depositMoneyRequest
                .flatMap(paymentService::depositMoney);
    }

    @Override
    public Mono<BalanceResponse> paymentsProcessPost(Mono<ProcessPaymentRequest> processPaymentRequest, ServerWebExchange exchange) {
        return processPaymentRequest
                .flatMap(paymentService::processPayment);
    }
}
