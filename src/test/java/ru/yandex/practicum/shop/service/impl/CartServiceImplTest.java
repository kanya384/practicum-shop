package ru.yandex.practicum.shop.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.shop.exception.AlreadyExistsInCartException;
import ru.yandex.practicum.shop.exception.NoItemInCartException;
import ru.yandex.practicum.shop.mapper.CartMapperImpl;
import ru.yandex.practicum.shop.mapper.ProductMapperImpl;
import ru.yandex.practicum.shop.model.Cart;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.service.CartService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CartServiceImpl.class, Cart.class, CartMapperImpl.class, ProductMapperImpl.class})
public class CartServiceImplTest {
    @Autowired
    private CartService cartService;

    @MockitoBean
    private ProductRepository productRepository;


    @BeforeEach
    void setUp() {
        cartService.clearCart();
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(new Product(2L, "title", "description", "image", 100)));
        cartService.addItemToCart(1L);
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void addItemToCart_shouldAdd() {
        when(productRepository.findById(2L))
                .thenReturn(Optional.of(new Product(2L, "title", "description", "image", 100)));

        cartService.addItemToCart(2L);

        assertEquals(2, cartService.getCartItems().size());
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void addItemToCart_shouldThrowExceptionIfItemAlreadyExistsInCart() {
        assertThrows(AlreadyExistsInCartException.class,
                () -> cartService.addItemToCart(
                        1L));
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void removeItemFromCart_shouldRemoveItem() {
        cartService.removeItemFromCart(1L);
        assertEquals(0, cartService.getCartItems().size());
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void increaseItemCount_shouldIncreaseItemCount() {
        cartService.increaseItemCount(1L);

        CartItem increasedItem = cartService.getCartItemById(1L);

        assertNotNull(increasedItem);
        assertEquals(2, increasedItem.getCount());
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void increaseItemCount_shouldThrowExceptionIfNoItemWithProvidedId() {
        assertThrows(NoItemInCartException.class,
                () -> cartService.increaseItemCount(2L));
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void decreaseItemCount_shouldDecreaseItemCount() {
        when(productRepository.findById(2L))
                .thenReturn(Optional.of(new Product(2L, "title", "description", "image", 100)));

        cartService.addItemToCart(2L);
        cartService.increaseItemCount(2L);

        CartItem item = cartService.getCartItemById(2L);

        assertNotNull(item);
        assertEquals(2, item.getCount());

        cartService.decreaseItemCount(2L);

        CartItem decreasedItem = cartService.getCartItemById(2L);

        assertNotNull(decreasedItem);
        assertEquals(1, decreasedItem.getCount());
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void decreaseItemCount_shouldRemoveOnDecreaseIfZeroCount() {
        cartService.decreaseItemCount(1L);

        assertEquals(0, cartService.getCartItems().size());
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void decreaseItemCount_shouldThrowExceptionIfNoItemWithProvidedId() {
        assertThrows(NoItemInCartException.class,
                () -> cartService.decreaseItemCount(2L));
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void clearCart_shouldClearCart() {
        cartService.clearCart();

        assertEquals(0, cartService.getCartItems().size());
    }
}
