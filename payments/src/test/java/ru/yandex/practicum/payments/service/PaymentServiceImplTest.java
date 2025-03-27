package ru.yandex.practicum.payments.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.payments.dto.ProcessPaymentRequest;
import ru.yandex.practicum.payments.model.BillingAccount;
import ru.yandex.practicum.payments.repository.BillingAccountRepository;
import ru.yandex.practicum.payments.service.impl.PaymentsServiceImpl;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {PaymentsServiceImpl.class})
public class PaymentServiceImplTest {
    @MockitoBean
    private BillingAccountRepository billingAccountRepository;

    @Autowired
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        reset(billingAccountRepository);
    }

    @Test
    void getBalance_shouldReturnBalance() {
        when(billingAccountRepository.findFirstByOrderByCreatedAt())
                .thenReturn(Mono.just(BillingAccount.builder()
                        .id(1L)
                        .money(1000)
                        .createdAt(LocalDate.now())
                        .modifiedAt(LocalDate.now())
                        .build()));

        StepVerifier.create(paymentService.getBalance())
                .expectNextMatches(b -> b.getMoney() != null
                        && b.getMoney() == 1000)
                .verifyComplete();
    }

    @Test
    void processPayment_shouldProcessPayment() {
        var request = new ProcessPaymentRequest();
        request.setOrderSum(900);

        when(billingAccountRepository.findFirstByOrderByCreatedAt())
                .thenReturn(Mono.just(BillingAccount.builder()
                        .id(1L)
                        .money(1000)
                        .createdAt(LocalDate.now())
                        .modifiedAt(LocalDate.now())
                        .build()));

        when(billingAccountRepository.save(any(BillingAccount.class)))
                .thenReturn(Mono.just(BillingAccount.builder()
                        .id(1L)
                        .money(100)
                        .createdAt(LocalDate.now())
                        .modifiedAt(LocalDate.now())
                        .build()));

        StepVerifier.create(paymentService.processPayment(request))
                .expectNextMatches(b -> b.getMoney() != null
                        && b.getMoney() == 100)
                .verifyComplete();

        verify(billingAccountRepository, times(1)).findFirstByOrderByCreatedAt();
        verify(billingAccountRepository, times(1)).save(any(BillingAccount.class));
    }

    @Test
    void depositMoney_shouldDepositMoney() {
        var request = new ProcessPaymentRequest();
        request.setOrderSum(900);

        when(billingAccountRepository.findFirstByOrderByCreatedAt())
                .thenReturn(Mono.just(BillingAccount.builder()
                        .id(1L)
                        .money(1000)
                        .createdAt(LocalDate.now())
                        .modifiedAt(LocalDate.now())
                        .build()));

        when(billingAccountRepository.save(any(BillingAccount.class)))
                .thenReturn(Mono.just(BillingAccount.builder()
                        .id(1L)
                        .money(500)
                        .createdAt(LocalDate.now())
                        .modifiedAt(LocalDate.now())
                        .build()));

        StepVerifier.create(paymentService.processPayment(request))
                .expectNextMatches(b -> b.getMoney() != null
                        && b.getMoney() == 500)
                .verifyComplete();

        verify(billingAccountRepository, times(1)).findFirstByOrderByCreatedAt();
        verify(billingAccountRepository, times(1)).save(any(BillingAccount.class));
    }
}
