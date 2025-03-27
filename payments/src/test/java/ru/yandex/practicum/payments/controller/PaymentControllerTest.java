package ru.yandex.practicum.payments.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.payments.dto.BalanceResponse;
import ru.yandex.practicum.payments.dto.DepositMoneyRequest;
import ru.yandex.practicum.payments.dto.ProcessPaymentRequest;
import ru.yandex.practicum.payments.service.PaymentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(PaymentController.class)
public class PaymentControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void paymentsBalanceGet_shouldReturnBalance() {
        var balanceResponse = new BalanceResponse();
        balanceResponse.setMoney(100);

        when(paymentService.getBalance())
                .thenReturn(Mono.just(balanceResponse));

        webTestClient.get()
                .uri("/payments/balance")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.money").isEqualTo(100);
    }

    @Test
    void paymentsProcessPost_shouldProcessPayment() {
        var balanceResponse = new BalanceResponse();
        balanceResponse.setMoney(500);

        var request = new ProcessPaymentRequest();
        request.setOrderSum(1500);

        when(paymentService.processPayment(request))
                .thenReturn(Mono.just(balanceResponse));

        webTestClient.post()
                .uri("/payments/process")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.money").isEqualTo(500);

        verify(paymentService, times(1))
                .processPayment(any(ProcessPaymentRequest.class));
    }

    @Test
    void paymentsDepositPost_shouldAddMoneyToBalance() {
        var balanceResponse = new BalanceResponse();
        balanceResponse.setMoney(2000);

        var request = new DepositMoneyRequest();
        request.setMoney(1500);

        when(paymentService.depositMoney(request))
                .thenReturn(Mono.just(balanceResponse));

        webTestClient.post()
                .uri("/payments/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.money").isEqualTo(2000);

        verify(paymentService, times(1))
                .depositMoney(any(DepositMoneyRequest.class));
    }
}
