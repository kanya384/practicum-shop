package ru.yandex.practicum.shop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.order.OrderItemResponseDTO;
import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.exception.NoProductsInOrderException;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.service.OrderService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@WebFluxTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    OrderService orderService;


    @Test
    void placeOrder_shouldPlaceOrder() {
        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .id(1L)
                .title("title")
                .description("description")
                .image("image")
                .price(100)
                .count(0)
                .build();

        OrderItemResponseDTO orderItemResponseDTO = OrderItemResponseDTO.builder()
                .id(1L)
                .product(productResponseDTO)
                .count(1)
                .build();

        OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                .id(1L)
                .items(List.of(orderItemResponseDTO))
                .createdAt(LocalDate.now())
                .build();

        when(orderService.placeOrder())
                .thenReturn(Mono.just(orderResponseDTO));

        webTestClient.post()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains(" <div class=\"order_item"));
                });

        verify(orderService, times(1)).placeOrder();
    }

    @Test
    void placeOrder_shouldReturnErrorIfNoItemsInOrder() {
        when(orderService.placeOrder())
                .thenThrow(new NoProductsInOrderException(""));

        webTestClient.post()
                .uri("/orders")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<h1>Упс, что-то пошло не так!</h1>"));
                });
    }

    @Test
    void findAll_shouldReturnOrdersList() {
        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .id(1L)
                .title("title")
                .description("description")
                .image("image")
                .price(100)
                .count(0)
                .build();

        OrderItemResponseDTO orderItemResponseDTO = OrderItemResponseDTO.builder()
                .id(1L)
                .product(productResponseDTO)
                .count(1)
                .build();

        OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                .id(1L)
                .items(List.of(orderItemResponseDTO))
                .createdAt(LocalDate.now())
                .build();

        when(orderService.findAll())
                .thenReturn(Flux.just(orderResponseDTO));

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
        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .id(1L)
                .title("title")
                .description("description")
                .image("image")
                .price(100)
                .count(0)
                .build();

        OrderItemResponseDTO orderItemResponseDTO = OrderItemResponseDTO.builder()
                .id(1L)
                .product(productResponseDTO)
                .count(1)
                .build();

        OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                .id(1L)
                .items(List.of(orderItemResponseDTO))
                .createdAt(LocalDate.now())
                .build();

        when(orderService.findById(anyLong()))
                .thenReturn(Mono.just(orderResponseDTO));

        webTestClient.get()
                .uri("/orders/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<div class=\"order_item"));
                });
    }

    @Test
    void findById_shouldReturn404IfOrderNotExists() {
        when(orderService.findById(anyLong()))
                .thenThrow(new ResourceNotFoundException("Заказ", -1L));

        webTestClient.get()
                .uri("/orders/{id}", -1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<h1>Упс, что-то пошло не так!</h1>"));
                });
    }


}
