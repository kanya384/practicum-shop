package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.api.PaymentsApi;
import ru.yandex.practicum.shop.domain.BalanceResponse;
import ru.yandex.practicum.shop.domain.ProcessPaymentRequest;
import ru.yandex.practicum.shop.service.PaymentsService;

@Service
@RequiredArgsConstructor
public class PaymentsServiceImpl implements PaymentsService {
    private final PaymentsApi paymentsApi;

    @Override
    public Mono<Integer> put5kToBalance(Long userId) {
        return paymentsApi.paymentsCreateUserIdPost(userId)
                .map(BalanceResponse::getMoney);
    }

    @Override
    public Mono<Integer> getBalance(Long userId) {
        return paymentsApi.paymentsBalanceUserIdGet(userId)
                .onErrorResume(t -> Mono.empty())
                .onErrorComplete()
                .map(BalanceResponse::getMoney);
    }

    @Override
    public Mono<Integer> processPayment(Long userId, int orderSum) {
        var request = new ProcessPaymentRequest();
        request.setOrderSum(orderSum);
        return paymentsApi.paymentsProcessUserIdPost(userId, request)
                .map(BalanceResponse::getMoney);
    }
}
