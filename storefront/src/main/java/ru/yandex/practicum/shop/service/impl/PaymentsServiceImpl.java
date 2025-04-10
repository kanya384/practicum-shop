package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.api.PaymentsApi;
import ru.yandex.practicum.shop.domain.BalanceResponse;
import ru.yandex.practicum.shop.domain.ProcessPaymentRequest;
import ru.yandex.practicum.shop.service.PaymentsService;
import ru.yandex.practicum.shop.utils.SecurityUtils;

@Service
@RequiredArgsConstructor
public class PaymentsServiceImpl implements PaymentsService {
    private final PaymentsApi paymentsApi;

    private final SecurityUtils securityUtils;

    @Override
    public Mono<Integer> put5kToBalance() {
        return securityUtils.getUserId()
                .flatMap(paymentsApi::paymentsCreateUserIdPost)
                .map(BalanceResponse::getMoney);
    }

    @Override
    public Mono<Integer> getBalance() {
        return securityUtils.getUserId()
                .flatMap(paymentsApi::paymentsBalanceUserIdGet)
                .onErrorResume(t -> Mono.empty())
                .onErrorComplete()
                .map(BalanceResponse::getMoney);
    }

    @Override
    public Mono<Integer> processPayment(int orderSum) {
        var request = new ProcessPaymentRequest();
        request.setOrderSum(orderSum);
        return securityUtils.getUserId()
                .flatMap(userId -> paymentsApi.paymentsProcessUserIdPost(userId, request))
                .map(BalanceResponse::getMoney);
    }
}
