package ru.yandex.practicum.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;
import ru.yandex.practicum.shop.repository.OrderRepository;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.OrderService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class OrderControllerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    OrderService orderService;

    @Autowired
    CartService cartService;

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll()
                .block();

        cartService.clearCart()
                .block();


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
    void placeOrder_shouldPlaceOrder() throws Exception {
        cartService
                .addItemToCart(1L)
                .doOnNext(c -> cartService.addItemToCart(2L))
                .block();

        webTestClient.post()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk();

        assertEquals(3, orderRepository.findAll().count().block());
    }

    @Test
    void placeOrder_shouldReturnErrorIfNoItemsInOrder() throws Exception {
        webTestClient.post()
                .uri("/orders")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void findAll_shouldReturnOrdersList() throws Exception {
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
    void findById_shouldReturnOrderById() throws Exception {
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
    void findById_shouldReturn404IfOrderNotExists() throws Exception {
        webTestClient.get()
                .uri("/orders/{id}", -1)
                .exchange()
                .expectStatus().isNotFound();
    }
}
