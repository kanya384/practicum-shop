package ru.yandex.practicum.shop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.cart.CartItemResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.exception.AlreadyExistsInCartException;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.PaymentsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@WebFluxTest(CartController.class)
public class CartControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    CartService cartService;

    @MockitoBean
    PaymentsService paymentsService;

    @Test
    void getCartItems_shouldReturnProductsList() {
        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder()
                .id(1L)
                .title("title")
                .description("description")
                .image("image")
                .price(100)
                .build();

        when(cartService.returnCartItems())
                .thenReturn(Mono.just(List.of(new CartItemResponseDTO(productResponseDTO, 1))));
        when(cartService.returnCartItems())
                .thenReturn(Mono.just(List.of(new CartItemResponseDTO(productResponseDTO, 1))));

        webTestClient.get()
                .uri("/cart")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("class=\"cart-item card"));
                });

        verify(cartService, times(2)).returnCartItems();
    }

    @Test
    void addProductToCart_shouldAddProductToCart() {
        when(cartService.addItemToCart(anyLong()))
                .thenReturn(Mono.just(3L));

        webTestClient.post()
                .uri("/cart/add/{id}", 3L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML);


        verify(cartService, times(1))
                .addItemToCart(anyLong());
    }


    @Test
    void addProductToCart_shouldReturnErrorIfProductAlreadyExistingInCart() {
        doThrow(new AlreadyExistsInCartException(""))
                .when(cartService)
                .addItemToCart(anyLong());

        webTestClient.post()
                .uri("/cart/add/{id}", 3L)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<h1>Упс, что-то пошло не так!</h1>"));
                });


        verify(cartService, times(1))
                .addItemToCart(anyLong());
    }

    @Test
    void removeProductFromCart_shouldRemoveProductFromCart() {
        when(cartService.removeItemFromCart(anyLong()))
                .thenReturn(Mono.just(3L));

        webTestClient.post()
                .uri("/cart/remove/{id}", 3L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML);


        verify(cartService, times(1))
                .removeItemFromCart(anyLong());
    }

    @Test
    void removeProductFromCart_shouldReturnErrorIfProductNotExistingInCart() {
        doThrow(new ResourceNotFoundException("Продутк", 3L)).when(cartService).removeItemFromCart(anyLong());

        webTestClient.post()
                .uri("/cart/remove/{id}", 3L)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<h1>Упс, что-то пошло не так!</h1>"));
                });
    }

    @Test
    void increaseItemCount_shouldIncreaseItemCount() {
        when(cartService.increaseItemCount(anyLong()))
                .thenReturn(Mono.just(3L));

        webTestClient.post()
                .uri("/cart/inc/{id}", 3L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML);

        verify(cartService, times(1))
                .increaseItemCount(any());
    }

    @Test
    void increaseItemCount_shouldReturnNotFoundIfNoItemInCart() {
        doThrow(new ResourceNotFoundException("Продутк", 3L))
                .when(cartService)
                .increaseItemCount(anyLong());

        webTestClient.post()
                .uri("/cart/inc/{id}", 3L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<h1>Упс, что-то пошло не так!</h1>"));
                });
    }

    @Test
    void decreaseItemCount_shouldIncreaseItemCount() {
        when(cartService.decreaseItemCount(anyLong()))
                .thenReturn(Mono.just(3L));

        webTestClient.post()
                .uri("/cart/dec/{id}", 3L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML);

        verify(cartService, times(1))
                .decreaseItemCount(any());
    }

    @Test
    void decreaseItemCount_shouldReturnNotFoundIfNoItemInCart() {
        doThrow(new ResourceNotFoundException("Продутк", 3L))
                .when(cartService)
                .decreaseItemCount(anyLong());

        webTestClient.post()
                .uri("/cart/dec/{id}", 3L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("<h1>Упс, что-то пошло не так!</h1>"));
                });
    }
}
