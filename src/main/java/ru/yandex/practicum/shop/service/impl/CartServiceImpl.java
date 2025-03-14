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

    public synchronized Mono<Void> addItemToCart(Long productId) {

        return Mono.just(cart.containsKey(productId))
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new AlreadyExistsInCartException(String.format("Продукт c id = %d уже в корзине", productId)));
                    }
                    return productRepository.findById(productId);
                })
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Продукт", productId)))
                .doOnNext((product) -> cart.put(productId, new CartItem(product, 1)))
                .then();
    }

    public Mono<Void> removeItemFromCart(Long productId) {
        return Mono.just(cart.containsKey(productId))
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new ResourceNotFoundException("Продукт", productId));
                    }

                    return Mono.just(productId);
                })
                .doOnNext((cart::remove))
                .then();
    }

    public synchronized Mono<Void> increaseItemCount(Long productId) {
        return Mono.just(cart.containsKey(productId))
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new NoItemInCartException(String.format("Продукта c id = %d нет в корзине", productId)));
                    }
                    return Mono.just(cart.get(productId));
                })
                .doOnNext(CartItem::inc)
                .then();
    }

    public synchronized Mono<Void> decreaseItemCount(Long productId) {
        return Mono.fromRunnable(() -> {
            CartItem item = cart.get(productId);
            if (item == null) {
                throw new NoItemInCartException(String.format("Продукта c id = %d нет в корзине", productId));
            }

            item.dec();

            if (item.getCount() == 0) {
                cart.remove(productId);
            }
        });
    }

    @Override
    public Mono<Void> clearCart() {
        return Mono.fromRunnable(cart::clear);
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
