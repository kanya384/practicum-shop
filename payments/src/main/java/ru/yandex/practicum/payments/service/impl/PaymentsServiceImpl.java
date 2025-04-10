package ru.yandex.practicum.payments.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.payments.dto.BalanceResponse;
import ru.yandex.practicum.payments.dto.DepositMoneyRequest;
import ru.yandex.practicum.payments.dto.ProcessPaymentRequest;
import ru.yandex.practicum.payments.exception.NotEnoughMoney;
import ru.yandex.practicum.payments.exception.ResourceNotFoundException;
import ru.yandex.practicum.payments.model.BillingAccount;
import ru.yandex.practicum.payments.repository.BillingAccountRepository;
import ru.yandex.practicum.payments.service.PaymentService;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentsServiceImpl implements PaymentService {
    private final BillingAccountRepository billingAccountRepository;

    @Override
    public Mono<BalanceResponse> createAccountAndPutMoneyToBalance(long userId) {
        return billingAccountRepository.findByUserId(userId)
                .switchIfEmpty(billingAccountRepository.save(new BillingAccount(userId)))
                .doOnNext(billingAccount -> billingAccount.setMoney(billingAccount.getMoney() + 5000))
                .flatMap(billingAccountRepository::save)
                .map(this::map);
    }

    @Override
    public Mono<BalanceResponse> getBalance(long userId) {
        return billingAccountRepository.findByUserId(userId)
                .switchIfEmpty(billingAccountRepository.save(new BillingAccount(userId)))
                .map(this::map);
    }


    @Override
    public Mono<BalanceResponse> processPayment(long userId, ProcessPaymentRequest request) {
        return billingAccountRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Не найден платежный аккаунт")))
                .doOnNext(ba -> ba.setMoney(ba.getMoney() - request.getOrderSum()))
                .flatMap(ba -> {
                    if (ba.getMoney() < 0) {
                        return Mono.error(new NotEnoughMoney("Недостаточно денег для совершения платежа"));
                    }
                    return billingAccountRepository.save(ba);
                })
                .map(this::map);
    }

    @Override
    public Mono<BalanceResponse> depositMoney(long userId, DepositMoneyRequest request) {
        return billingAccountRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Не найден платежный аккаунт")))
                .doOnNext(ba -> ba.setMoney(ba.getMoney() + request.getMoney()))
                .flatMap(billingAccountRepository::save)
                .map(this::map);
    }

    private BalanceResponse map(BillingAccount p) {
        var br = new BalanceResponse();
        br.setMoney(p.getMoney());
        return br;
    }
}
