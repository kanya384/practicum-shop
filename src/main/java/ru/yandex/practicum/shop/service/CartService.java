package ru.yandex.practicum.shop.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.cart.CartItemResponseDTO;
import ru.yandex.practicum.shop.model.CartItem;

import java.util.Map;

public interface CartService {
    Mono<Void> addItemToCart(Long productId);

    Mono<Void> removeItemFromCart(Long productId);

    Mono<Void> increaseItemCount(Long productId);

    Mono<Void> decreaseItemCount(Long productId);

    Mono<Void> clearCart();

    Flux<CartItem> getCartItems();

    Map<Long, CartItem> getProductsInCartMap();

    Mono<CartItem> getCartItemById(Long id);

    Flux<CartItemResponseDTO> returnCartItems();
}
