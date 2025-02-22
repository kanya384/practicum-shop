package ru.yandex.practicum.shop.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.shop.exception.AlreadyExistsInCartException;
import ru.yandex.practicum.shop.exception.NoItemInCartException;
import ru.yandex.practicum.shop.mapper.CartMapperImpl;
import ru.yandex.practicum.shop.mapper.ProductMapperImpl;
import ru.yandex.practicum.shop.model.Cart;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.service.CartService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CartServiceImpl.class, Cart.class, CartMapperImpl.class, ProductMapperImpl.class})
public class CartServiceImplTest {
    @Autowired
    private CartService cartService;


    @BeforeEach
    void setUp() {
        cartService.clearCart();

        cartService.addItemToCart(new Product(1L, "title", "description", "image", 100));
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void addItemToCart_shouldAdd() {
        cartService.addItemToCart(new Product(2L, "title", "description", "image", 100));

        assertEquals(2, cartService.getCartItems().size());
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void addItemToCart_shouldThrowExceptionIfItemAlreadyExistsInCart() {
        assertThrows(AlreadyExistsInCartException.class,
                () -> cartService.addItemToCart(
                        new Product(1L, "title", "description", "image", 100)));
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void removeItemFromCart_shouldRemoveItem() {
        cartService.removeItemFromCart(1L);
        assertEquals(0, cartService.getCartItems().size());
    }

    @RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)
    void increaseItemCount_shouldIncreaseItemCount() {
        cartService.increaseItemCount(1L);

        CartItem increasedItem = cartService.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId() == 1L)
                .findFirst()
                .orElse(null);

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
        cartService.addItemToCart(new Product(2L, "title", "description", "image", 100));
        cartService.increaseItemCount(2L);

        CartItem item = cartService.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId() == 2L)
                .findFirst()
                .orElse(null);

        assertNotNull(item);
        assertEquals(2, item.getCount());

        cartService.decreaseItemCount(2L);

        CartItem decreasedItem = cartService.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId() == 1L)
                .findFirst()
                .orElse(null);


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
