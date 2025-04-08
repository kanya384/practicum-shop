package ru.yandex.practicum.shop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductSort;
import ru.yandex.practicum.shop.dto.product.ProductsPageResponseDTO;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.ProductService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private CartService cartService;

    @Test
    void productsList_shouldReturnProductsList() throws Exception {
        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .id(1L)
                .title("title")
                .description("description")
                .image("image")
                .price(100)
                .build();

        ProductsPageResponseDTO productsPageResponseDTO = ProductsPageResponseDTO
                .builder()
                .page(1)
                .pageSize(10)
                .totalCount(100)
                .list(List.of(productResponseDTO))
                .build();


        when(productService.findAll(null, 1, 10, ProductSort.EMPTY))
                .thenReturn(Mono.just(productsPageResponseDTO));

        when(cartService.getProductsInCartMap())
                .thenReturn(Mono.just(Map.of()));

        webTestClient.get()
                .uri("/products")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<div class=\"product"));
                });
    }


    @Test
    void findById_shouldReturnProductById() throws Exception {
        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .id(1L)
                .title("title")
                .description("description")
                .image("image")
                .price(100)
                .build();

        when(productService.findById(1L))
                .thenReturn(Mono.just(productResponseDTO));
        when(cartService.getCartItemById(1L))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/products/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<div class=\"product"));
                });
    }

    @Test
    void findById_shouldReturn404Page() throws Exception {
        when(productService.findById(any()))
                .thenThrow(new ResourceNotFoundException("Продукт", -1L));

        webTestClient.get()
                .uri("/products/{id}", -1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<h1>Упс, что-то пошло не так!</h1>"));
                });
    }
}
