package ru.yandex.practicum.payments.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.payments.configuration.SecurityConfig;
import ru.yandex.practicum.payments.dto.BalanceResponse;
import ru.yandex.practicum.payments.dto.DepositMoneyRequest;
import ru.yandex.practicum.payments.dto.ProcessPaymentRequest;
import ru.yandex.practicum.payments.service.PaymentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest({PaymentController.class, SecurityConfig.class})
public class PaymentControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    @WithMockUser
    void paymentsBalanceGet_shouldReturnBalance() {
        var balanceResponse = new BalanceResponse();
        balanceResponse.setMoney(100);

        when(paymentService.getBalance(any(Long.class)))
                .thenReturn(Mono.just(balanceResponse));

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri("/payments/balance/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.money").isEqualTo(100);
    }

    @Test
    @WithMockUser
    void paymentsProcessPost_shouldProcessPayment() {
        var balanceResponse = new BalanceResponse();
        balanceResponse.setMoney(500);

        var request = new ProcessPaymentRequest();
        request.setOrderSum(1500);

        when(paymentService.processPayment(any(Long.class), any()))
                .thenReturn(Mono.just(balanceResponse));

        webTestClient
                .post()
                .uri("/payments/process/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.money").isEqualTo(500);

        verify(paymentService, times(1))
                .processPayment(any(Long.class), any(ProcessPaymentRequest.class));
    }

    @Test
    @WithMockUser
    void paymentsDepositPost_shouldAddMoneyToBalance() {
        var balanceResponse = new BalanceResponse();
        balanceResponse.setMoney(2000);

        var request = new DepositMoneyRequest();
        request.setMoney(1500);

        when(paymentService.depositMoney(any(Long.class), any()))
                .thenReturn(Mono.just(balanceResponse));

        webTestClient.post()
                .uri("/payments/deposit/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.money").isEqualTo(2000);

        verify(paymentService, times(1))
                .depositMoney(any(Long.class), any(DepositMoneyRequest.class));
    }
}
