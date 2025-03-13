package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;
import ru.yandex.practicum.shop.exception.NoProductsInOrderException;
import ru.yandex.practicum.shop.mapper.OrderMapper;
import ru.yandex.practicum.shop.model.Order;
import ru.yandex.practicum.shop.model.OrderItem;
import ru.yandex.practicum.shop.repository.OrderItemRepository;
import ru.yandex.practicum.shop.repository.OrderRepository;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final CartService cartService;

    @Transactional
    @Override
    public Mono<OrderResponseDTO> placeOrder() {
        Order order = new Order();

        return cartService.getCartItems()
                .switchIfEmpty(Mono.error(new NoProductsInOrderException("В заказе должен быть хотя бы один товар")))
                .doOnNext(cartItem -> {
                            OrderItem orderItem = OrderMapper.map(cartItem);
                            orderItem.setOrder(order);
                            order.addItem(orderItem);
                        }
                )
                .collectList()
                .flatMap(__ -> orderRepository.save(order))
                .switchIfEmpty(Mono.error(new InternalError("Ошибка сохранения заказа")))
                .doOnNext(o -> orderItemRepository.saveAll(order.getItems()))
                .switchIfEmpty(Mono.error(new InternalError("Ошибка сохранения заказа")))
                .doOnNext(o -> cartService.clearCart())
                .map(orderMapper::map);
    }

    @Override
    public Mono<OrderResponseDTO> findById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::map);
    }

    @Override
    public Flux<OrderResponseDTO> findAll() {
        return orderRepository
                .findAlOrdersWithOrderItems()
                .map(orderMapper::map);
    }
}
