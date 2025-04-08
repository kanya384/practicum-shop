package ru.yandex.practicum.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.yandex.practicum.shop.api.PaymentsApi;
import ru.yandex.practicum.shop.domain.DepositMoneyRequest;
import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;
import ru.yandex.practicum.shop.repository.OrderRepository;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.OrderService;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureWebTestClient
@DirtiesContext
@Tag("integration")
public class OrderControllerIntegrationTest extends AbstractTestContainer {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartService cartService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private PaymentsApi paymentsApi;

    @BeforeEach
    void setUp() {

        orderRepository.deleteAll()
                .block();

        cartService.clearCart();

        assertDoesNotThrow(() -> {
            var deposit = new DepositMoneyRequest();
            deposit.setMoney(10000);
            paymentsApi.paymentsDepositPost(deposit)
                    .block();
        });


        cartService.addItemToCart(1L)
                .doOnNext(c -> cartService.addItemToCart(2L))
                .block();
        orderService.placeOrder().block();

        cartService.addItemToCart(3L)
                .doOnNext(c -> cartService.addItemToCart(4L))
                .block();
        orderService.placeOrder().block();
    }

    @Test
    void placeOrder_shouldPlaceOrder() {
        cartService
                .addItemToCart(1L)
                .block();

        webTestClient.post()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk();

        assertEquals(3, orderRepository.findAll().count().block());
    }

    @Test
    void placeOrder_shouldReturnErrorIfNoItemsInOrder() {
        webTestClient.post()
                .uri("/orders")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void placeOrder_shouldReturnErrorIfNotEnoughMoney() {
        /*cartService.addItemToCart(1L, CartItem.builder()
                .product(new Product(999L, "", "", "", 10000))
                .count(1000)
                .build());*/

        webTestClient.post()
                .uri("/orders")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("Недостаточно средств для совершения заказа"));
                });
    }

    @Test
    void findAll_shouldReturnOrdersList() {
        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<div class=\"order-item"));
                });
    }

    @Test
    void findById_shouldReturnOrderById() {
        cartService.addItemToCart(1L)
                .block();
        OrderResponseDTO order = orderService.placeOrder()
                .block();
        assertNotNull(order);

        webTestClient.get()
                .uri("/orders/{id}", order.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<div class=\"order_item"));
                });
    }

    @Test
    void findById_shouldReturn404IfOrderNotExists() {
        webTestClient.get()
                .uri("/orders/{id}", -1)
                .exchange()
                .expectStatus().isNotFound();
    }
}
