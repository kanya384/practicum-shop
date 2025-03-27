package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.cart.CartItemResponseDTO;
import ru.yandex.practicum.shop.exception.AlreadyExistsInCartException;
import ru.yandex.practicum.shop.exception.NoItemInCartException;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.mapper.CartMapper;
import ru.yandex.practicum.shop.model.Cart;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.service.CartService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final Cart cart;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    public synchronized Mono<Long> addItemToCart(Long productId) {
        return Mono.just(cart.containsKey(productId))
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new AlreadyExistsInCartException(String.format("Продукт c id = %d уже в корзине", productId)));
                    }
                    return productRepository.findById(productId);
                })
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Продукт", productId)))
                .doOnNext((product) -> cart.put(productId, new CartItem(product, 1)))
                .map(p -> productId);
    }

    public Mono<Long> removeItemFromCart(Long productId) {
        return Mono.just(cart.containsKey(productId))
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new ResourceNotFoundException("Продукт", productId));
                    }

                    return Mono.just(productId);
                })
                .doOnNext((cart::remove))
                .map(p -> productId);
    }

    public synchronized Mono<Long> increaseItemCount(Long productId) {
        return Mono.just(cart.containsKey(productId))
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new NoItemInCartException(String.format("Продукта c id = %d нет в корзине", productId)));
                    }
                    return Mono.just(cart.get(productId));
                })
                .doOnNext(CartItem::inc)
                .map(ci -> cart.put(ci.getProduct().getId(), ci))
                .map(p -> productId);
    }

    public synchronized Mono<Long> decreaseItemCount(Long productId) {
        return Mono.just(cart.containsKey(productId))
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new NoItemInCartException(String.format("Продукта c id = %d нет в корзине", productId)));
                    }
                    return Mono.just(cart.get(productId));
                })
                .doOnNext(CartItem::dec)
                .doOnNext(ci -> {
                    if (ci.getCount() == 0) {
                        cart.remove(productId);
                    } else {
                        cart.put(ci.getProduct().getId(), ci);
                    }
                })
                .map(p -> productId);
    }

    @Override
    public void clearCart() {
        cart.clear();
    }

    public List<CartItem> getCartItems() {
        return cart.values().stream().toList();
    }

    @Override
    public Map<Long, CartItem> getProductsInCartMap() {
        return Collections.unmodifiableMap(cart);
    }

    @Override
    public Mono<CartItem> getCartItemById(Long id) {
        var cartItem = cart.get(id);
        return cartItem != null ? Mono.just(cartItem) : Mono.empty();
    }

    public Flux<CartItemResponseDTO> returnCartItems() {
        return Flux.fromIterable(cart.values())
                .map(cartMapper::map);
    }
}
