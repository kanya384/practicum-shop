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
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.repository.CartItemRepository;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.service.CartService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    //TODO: get user_id from Authorization
    private final Long userId = 1L;

    public synchronized Mono<Long> addItemToCart(Long productId) {
        return Mono.just(cartItemRepository.findByUserIdAndProductId(userId, productId))
                .switchIfEmpty(Mono.error(new AlreadyExistsInCartException(String.format("Продукт c id = %d уже в корзине", productId))))
                .flatMap(ci -> productRepository.findById(productId))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Продукт", productId)))
                .doOnNext((product) -> cartItemRepository.save(new CartItem(userId, productId, 1)))
                .map(p -> productId);
    }

    public Mono<Long> removeItemFromCart(Long productId) {
        return cartItemRepository.findByUserIdAndProductId(userId, productId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Продукт", productId)))
                .doOnNext(cartItem -> cartItemRepository.deleteById(cartItem.getId()))
                .map(CartItem::getId);
    }

    public synchronized Mono<Long> increaseItemCount(Long productId) {
        return cartItemRepository.findByUserIdAndProductId(userId, productId)
                .switchIfEmpty(Mono.error(new NoItemInCartException(String.format("Продукта c id = %d нет в корзине", productId))))
                .doOnNext(CartItem::inc)
                .flatMap(cartItemRepository::save)
                .map(CartItem::getId);
    }

    public synchronized Mono<Long> decreaseItemCount(Long productId) {
        return cartItemRepository.findByUserIdAndProductId(userId, productId)
                .switchIfEmpty(Mono.error(new NoItemInCartException(String.format("Продукта c id = %d нет в корзине", productId))))
                .doOnNext(CartItem::dec)
                .doOnNext(ci -> {
                    if (ci.getCount() == 0) {
                        cartItemRepository.deleteById(ci.getId());
                    } else {
                        cartItemRepository.save(ci);
                    }
                })
                .map(CartItem::getId);
    }

    @Override
    public Mono<Void> clearCart() {
        return cartItemRepository.deleteByUserId(userId);
    }

    public Flux<CartItem> getCartItems() {
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    public Mono<Map<Long, CartItem>> getProductsInCartMap() {
        return cartItemRepository.findByUserId(userId)
                .collectMap(CartItem::getId, ci -> ci);
    }

    @Override
    public Mono<Integer> getSumOfCartItems() {
        return cartItemRepository.findByUserId(userId)
                .zipWith(findProductsOfCartMap())
                .map(tuple -> {
                    var cartItem = tuple.getT1();
                    var productsMap = tuple.getT2();
                    var product = productsMap.get(cartItem.getId());

                    return cartItem.getCount() * product.getPrice();
                })
                .reduce(0, Integer::sum);
    }

    public Mono<Map<Long, Product>> findProductsOfCartMap() {
        return cartItemRepository.findByUserId(userId)
                .map(CartItem::getProductId)
                .collectList()
                .map(productRepository::findAllById)
                .flatMap(p -> p.collectMap(Product::getId, pr -> pr));
    }

    @Override
    public Mono<Long> getCountOfCartItems() {
        return cartItemRepository.countByUserId(userId);
    }

    @Override
    public Mono<CartItem> getCartItemById(Long id) {
        return cartItemRepository.findById(id);
    }

    public Flux<CartItemResponseDTO> returnCartItems() {
        return cartItemRepository.findByUserId(userId)
                .map(cartMapper::map);
    }
}
