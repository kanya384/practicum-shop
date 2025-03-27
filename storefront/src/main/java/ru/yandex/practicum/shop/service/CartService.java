package ru.yandex.practicum.shop.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.cart.CartItemResponseDTO;
import ru.yandex.practicum.shop.model.CartItem;

import java.util.List;
import java.util.Map;

public interface CartService {
    Mono<Long> addItemToCart(Long productId);

    Mono<Long> removeItemFromCart(Long productId);

    Mono<Long> increaseItemCount(Long productId);

    Mono<Long> decreaseItemCount(Long productId);

    void clearCart();

    List<CartItem> getCartItems();

    Map<Long, CartItem> getProductsInCartMap();

    Mono<CartItem> getCartItemById(Long id);

    Flux<CartItemResponseDTO> returnCartItems();
}
