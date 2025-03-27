package ru.yandex.practicum.shop.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.shop.exception.AlreadyExistsInCartException;
import ru.yandex.practicum.shop.exception.NoItemInCartException;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.mapper.CartMapperImpl;
import ru.yandex.practicum.shop.mapper.ProductMapperImpl;
import ru.yandex.practicum.shop.model.Cart;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.service.CartService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CartServiceImpl.class, Cart.class, CartMapperImpl.class, ProductMapperImpl.class})
public class CartServiceImplTest {
    @Autowired
    private Cart cart;

    @Autowired
    private CartService cartService;

    @MockitoBean
    private ProductRepository productRepository;


    @BeforeEach
    void setUp() {
        cart.clear();

        Product product = new Product(1L, "title", "description", "image", 100);

        cart.put(1L, new CartItem(product, 1));
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void addItemToCart_shouldAdd() {
        when(productRepository.findById(any(Long.class)))
                .thenReturn(Mono.just(new Product(2L, "title", "description", "image", 100)));

        StepVerifier.create(cartService.addItemToCart(2L))
                .expectNextMatches(id -> id == 2L)
                .verifyComplete();

        assertEquals(2, cart.size());
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void addItemToCart_shouldThrowExceptionIfNoItemInRepository() {
        when(productRepository.findById(any(Long.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(cartService.addItemToCart(2L))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void addItemToCart_shouldThrowExceptionIfItemAlreadyExistsInCart() {
        StepVerifier.create(cartService.addItemToCart(1L))
                .expectError(AlreadyExistsInCartException.class)
                .verify();
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void removeItemFromCart_shouldRemoveItem() {
        StepVerifier.create(cartService.removeItemFromCart(1L))
                .expectNextMatches(id -> id == 1L)
                .verifyComplete();

        assertEquals(0, cart.size());
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void removeItemFromCart_shouldThrowExceptionIfNoItemWithProvidedId() {
        StepVerifier.create(cartService.removeItemFromCart(2L))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void increaseItemCount_shouldIncreaseItemCount() {
        assertEquals(1, cart.get(1L).getCount());

        StepVerifier.create(cartService.increaseItemCount(1L))
                .expectNextMatches(id -> id == 1L)
                .verifyComplete();

        assertEquals(2, cart.get(1L).getCount());
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void increaseItemCount_shouldThrowExceptionIfNoItemWithProvidedId() {
        StepVerifier.create(cartService.increaseItemCount(2L))
                .expectError(NoItemInCartException.class)
                .verify();
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void decreaseItemCount_shouldDecreaseItemCount() {
        cartService.increaseItemCount(1L)
                .block();

        assertEquals(2, cart.get(1L).getCount());

        StepVerifier
                .create(cartService.decreaseItemCount(1L))
                .expectNextMatches(id -> id == 1L)
                .verifyComplete();

        assertEquals(1, cart.get(1L).getCount());
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void decreaseItemCount_shouldRemoveOnDecreaseIfZeroCount() {

        StepVerifier.create(cartService.decreaseItemCount(1L))
                .expectNextMatches(id -> id == 1L)
                .verifyComplete();

        assertEquals(0, cart.size());
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void decreaseItemCount_shouldThrowExceptionIfNoItemWithProvidedId() {
        StepVerifier.create(cartService.decreaseItemCount(2L))
                .expectError(NoItemInCartException.class);
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void clearCart_shouldClearCart() {
        cartService.clearCart();

        assertEquals(0, cart.size());
    }
}
