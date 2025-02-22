package ru.yandex.practicum.shop.service;

import ru.yandex.practicum.shop.dto.cart.CartItemResponseDTO;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Product;

import java.util.List;

public interface CartService {
    void addItemToCart(Product product);

    void removeItemFromCart(Long productId);

    void increaseItemCount(Long productId);

    void decreaseItemCount(Long productId);

    void clearCart();

    List<CartItem> getCartItems();

    List<CartItemResponseDTO> returnCartItems();
}
