package ru.yandex.practicum.shop.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;

public interface OrderService {
    Mono<OrderResponseDTO> placeOrder();

    Mono<OrderResponseDTO> findById(Long id);

    Flux<OrderResponseDTO> findAll();
}
