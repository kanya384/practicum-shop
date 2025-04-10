package ru.yandex.practicum.payments.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.payments.model.BillingAccount;

@Repository
public interface BillingAccountRepository extends R2dbcRepository<BillingAccount, Long> {
    Mono<BillingAccount> findByUserId(long userId);
}