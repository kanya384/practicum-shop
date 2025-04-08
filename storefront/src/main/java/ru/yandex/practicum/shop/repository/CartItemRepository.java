package ru.yandex.practicum.shop.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.model.CartItem;

@Repository
public interface CartItemRepository extends R2dbcRepository<CartItem, Long> {
    Mono<CartItem> findByUserIdAndProductId(Long userId, Long productId);

    Flux<CartItem> findByUserId(Long userId);

    Mono<Long> countByUserId(Long userId);

    Mono<Void> deleteByUserId(Long userId);
}
