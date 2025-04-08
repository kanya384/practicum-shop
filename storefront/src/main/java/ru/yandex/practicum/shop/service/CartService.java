package ru.yandex.practicum.shop.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.cart.CartItemResponseDTO;
import ru.yandex.practicum.shop.model.CartItem;

import java.util.Map;

public interface CartService {
    Mono<Long> addItemToCart(Long productId);

    Mono<Long> removeItemFromCart(Long productId);

    Mono<Long> increaseItemCount(Long productId);

    Mono<Long> decreaseItemCount(Long productId);

    Mono<Void> clearCart();

    Flux<CartItem> getCartItems();

    Mono<Map<Long, CartItem>> getProductsInCartMap();

    Mono<Integer> getSumOfCartItems();

    Mono<Long> getCountOfCartItems();

    Mono<CartItem> getCartItemById(Long id);

    Flux<CartItemResponseDTO> returnCartItems();
}
