package ru.yandex.practicum.shop.service;

import ru.yandex.practicum.shop.dto.cart.CartItemResponseDTO;
import ru.yandex.practicum.shop.model.CartItem;

import java.util.List;
import java.util.Map;

public interface CartService {
    void addItemToCart(Long productId);

    void removeItemFromCart(Long productId);

    void increaseItemCount(Long productId);

    void decreaseItemCount(Long productId);

    void clearCart();

    List<CartItem> getCartItems();

    Map<Long, CartItem> getProductsInCartMap();

    List<CartItemResponseDTO> returnCartItems();
}
