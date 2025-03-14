package ru.yandex.practicum.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.shop.dto.order.OrderItemResponseDTO;
import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.exception.NoProductsInOrderException;
import ru.yandex.practicum.shop.mapper.OrderMapper;
import ru.yandex.practicum.shop.mapper.ProductMapper;
import ru.yandex.practicum.shop.model.Order;
import ru.yandex.practicum.shop.model.OrderItem;
import ru.yandex.practicum.shop.model.Product;
import ru.yandex.practicum.shop.repository.OrderItemRepository;
import ru.yandex.practicum.shop.repository.OrderRepository;
import ru.yandex.practicum.shop.repository.ProductRepository;
import ru.yandex.practicum.shop.service.CartService;
import ru.yandex.practicum.shop.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private final CartService cartService;

    @Transactional
    @Override
    public Mono<Long> placeOrder() {
        return orderRepository.save(new Order())
                .switchIfEmpty(Mono.error(new InternalError("ошибка сохранения заказа")))
                .map(order -> cartService.getCartItems()
                        .stream()
                        .map(ci -> {
                            OrderItem orderItem = new OrderItem();
                            orderItem.setProductId(ci.getProduct().getId());
                            orderItem.setCount(ci.getCount());
                            orderItem.setOrderId(order.getId());
                            return orderItem;
                        }).toList()
                )
                .map(orderItemRepository::saveAll)
                .switchIfEmpty(Mono.error(new NoProductsInOrderException("в заказе должен быть хотя бы один продукт")))
                .flatMap(Flux::collectList)
                .map(oi -> oi.getFirst().getOrderId());
    }

    @Override
    public Mono<OrderResponseDTO> findById(Long id) {
        return orderRepository.findById(id)
                .zipWith(findOrderItemsByOrderId(id))
                .map(tuple ->
                        OrderResponseDTO.builder()
                                .id(tuple.getT1().getId())
                                .items(tuple.getT2())
                                .sum(tuple.getT2().stream()
                                        .map(OrderItemResponseDTO::getTotalPrice)
                                        .reduce(0, Integer::sum))
                                .createdAt(tuple.getT1().getCreatedAt())
                                .build()
                );
    }

    @Override
    public Flux<OrderResponseDTO> findAll() {
        return orderRepository.findAll()
                .map(orderMapper::map);
    }

    private Mono<List<OrderItemResponseDTO>> findOrderItemsByOrderId(Long orderId) {
        return findProductsInOrderMapByOrderId(orderId)
                .zipWith(orderItemRepository.findAllByOrderId(orderId)
                        .collectList())
                .map(tuple -> {
                            List<OrderItem> orderItems = tuple.getT2();
                            List<OrderItemResponseDTO> result = new ArrayList<>();
                            orderItems.forEach(orderItem -> {
                                ProductResponseDTO product = tuple.getT1().get(orderItem.getProductId());

                                result.add(OrderItemResponseDTO.builder()
                                        .id(orderItem.getId())
                                        .product(product)
                                        .totalPrice(product.getPrice() * orderItem.getCount())
                                        .count(orderItem.getCount())
                                        .build());
                            });


                            return result;
                        }
                );
    }

    private Mono<Map<Long, ProductResponseDTO>> findProductsInOrderMapByOrderId(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId)
                .map(OrderItem::getProductId)
                .collectList()
                .map(productRepository::findAllById)
                .flatMap(p -> p.collectMap(
                        Product::getId,
                        productMapper::map
                ));
    }
}
