package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.cart.CartItemResponseDTO;
import ru.yandex.practicum.shop.exception.NoItemInCartException;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.mapper.CartMapper;
import ru.yandex.practicum.shop.mapper.ProductMapper;
import ru.yandex.practicum.shop.model.CartItem;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.repository.CartItemRepository;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;
    private final SecurityUtils securityUtils;


    public synchronized Mono<Long> addItemToCart(Long productId) {
        return securityUtils.getUserId()
                .flatMap((userId) -> cartItemRepository.save(new CartItem(userId, productId, 1)))
                .map(p -> productId);
    }

    public Mono<Long> removeItemFromCart(Long productId) {

        return securityUtils.getUserId()
                .flatMap(userId -> cartItemRepository.findByUserIdAndProductId(userId, productId))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Продукт", productId)))
                .flatMap(cartItem -> cartItemRepository.deleteById(cartItem.getId()).thenReturn(productId));
    }

    public synchronized Mono<Long> increaseItemCount(Long productId) {
        return securityUtils.getUserId()
                .flatMap(userId -> cartItemRepository.findByUserIdAndProductId(userId, productId))
                .switchIfEmpty(Mono.error(new NoItemInCartException(String.format("Продукта c id = %d нет в корзине", productId))))
                .doOnNext(CartItem::inc)
                .flatMap(cartItemRepository::save)
                .map(CartItem::getId);
    }

    public synchronized Mono<Long> decreaseItemCount(Long productId) {
        return securityUtils.getUserId()
                .flatMap(userId -> cartItemRepository.findByUserIdAndProductId(userId, productId))
                .switchIfEmpty(Mono.error(new NoItemInCartException(String.format("Продукта c id = %d нет в корзине", productId))))
                .doOnNext(CartItem::dec)
                .flatMap(ci -> {
                    if (ci.getCount() == 0) {
                        return cartItemRepository.deleteById(ci.getId())
                                .thenReturn(ci.getId());
                    } else {
                        return cartItemRepository.save(ci).
                                map(p -> ci.getId());
                    }
                });
    }

    @Override
    public Mono<Void> clearCart() {
        return securityUtils.getUserId()
                .flatMap(cartItemRepository::deleteByUserId);
    }

    public Flux<CartItem> getCartItems() {
        return securityUtils.getUserId()
                .flatMapMany(cartItemRepository::findByUserId);
    }

    public Flux<CartItem> getCartItemsOfUser(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    public Mono<Map<Long, CartItem>> getProductsInCartMap() {
        return securityUtils.getUserId()
                .flatMapMany(cartItemRepository::findByUserId)
                .collectMap(CartItem::getProductId, ci -> ci);
    }

    @Override
    public Mono<Integer> getSumOfCartItems() {
        return securityUtils.getUserId().flatMapMany(cartItemRepository::findByUserId)
                .zipWith(findProductsOfCartMap())
                .map(tuple -> {
                    var cartItem = tuple.getT1();
                    var productsMap = tuple.getT2();
                    var product = productsMap.get(cartItem.getProductId());

                    return cartItem.getCount() * product.getPrice();
                })
                .reduce(0, Integer::sum);
    }

    public Mono<Map<Long, Product>> findProductsOfCartMap() {
        return securityUtils.getUserId().flatMapMany(cartItemRepository::findByUserId)
                .map(CartItem::getProductId)
                .collectList()
                .map(productRepository::findAllById)
                .flatMap(p -> p.collectMap(Product::getId, pr -> pr));
    }

    @Override
    public Mono<Long> getCountOfCartItems() {
        return securityUtils.getUserId()
                .flatMap(cartItemRepository::countByUserId);
    }

    @Override
    public Mono<CartItem> getCartItemByProductId(Long productId) {
        return securityUtils.getUserId()
                .flatMap(userId -> cartItemRepository.findByUserIdAndProductId(userId, productId));
    }

    public Mono<List<CartItemResponseDTO>> returnCartItems() {
        return securityUtils.getUserId()
                .flatMapMany(cartItemRepository::findByUserId)
                .collectList()
                .zipWith(findProductsOfCartMap())
                .map(tuple -> {
                    var cartItems = tuple.getT1();
                    var products = tuple.getT2();

                    List<CartItemResponseDTO> result = new ArrayList<>();

                    for (var cartItem : cartItems) {
                        var cartResponseDto = cartMapper.map(cartItem);
                        cartResponseDto.setProduct(productMapper.map(products.get(cartItem.getProductId())));
                        result.add(cartResponseDto);
                    }

                    return result;
                });
    }

    public Mono<Integer> cartSum() {
        return returnCartItems()
                .map(cartItems -> cartItems
                        .stream()
                        .map(ci -> ci.getCount() * ci.getProduct().getPrice())
                        .reduce(0, Integer::sum));
    }
}
