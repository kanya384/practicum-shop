package ru.yandex.practicum.payments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.yandex.practicum.payments.dto.DepositMoneyRequest;
import ru.yandex.practicum.payments.dto.ProcessPaymentRequest;
import ru.yandex.practicum.payments.model.BillingAccount;
import ru.yandex.practicum.payments.repository.BillingAccountRepository;
import ru.yandex.practicum.payments.service.PaymentService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class PaymentControllerIntegrationTest extends AbstractTestContainer {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BillingAccountRepository billingAccountRepository;

    @BeforeEach
    void setUp() {
        billingAccountRepository.deleteAll()
                .block();

        var billingAccount = BillingAccount.builder()
                .money(100)
                .build();

        billingAccountRepository.save(billingAccount).block();
    }

    @Test
    void paymentsBalanceGet_shouldReturnBalance() {
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
        var request = new ProcessPaymentRequest();
        request.setOrderSum(50);

        webTestClient.post()
                .uri("/payments/process")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.money").isEqualTo(50);
    }

    @Test
    void paymentsProcessPost_shouldReturnNotEnoughMoney() {
        var request = new ProcessPaymentRequest();
        request.setOrderSum(500);

        webTestClient.post()
                .uri("/payments/process")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Недостаточно денег для совершения платежа");
    }

    @Test
    void paymentsDepositPost_shouldAddMoneyToBalance() {
        var request = new DepositMoneyRequest();
        request.setMoney(1500);

        webTestClient.post()
                .uri("/payments/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.money").isEqualTo(1600);
    }
}
